package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.TransactionHistoryRepository;
import com.gym.gymmanagementsystem.repositories.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public List<UserSubscription> getAllUserSubscriptions() {
        return userSubscriptionRepository.findAll();
    }

    @Override
    public Optional<UserSubscription> getUserSubscriptionById(Integer id) {
        return userSubscriptionRepository.findById(id);
    }
    /**
     * Hlavní logika pro přiřazení/prodloužení předplatného.
     * - najde uživatelovo aktivní subscription (pokud existuje)
     * - pokud existuje:
     *     • pokud je customEndDate != null, nastaví nový konec na customEndDate
     *     • jinak prodlouží o původní počet měsíců z předplatného
     * - pokud neexistuje žádné platné předplatné:
     *     • deaktivuje všechna aktivní předplatná
     *     • vytvoří nové od dneška do customEndDate nebo do dne + durationMonths
     * - v obou případech vytvoří záznam do TransactionHistory, pro subscriptionID=6 použije customPrice
     */
    @Override
    public UserSubscription createUserSubscription(
            UserSubscription request,
            LocalDate customEndDate,
            BigDecimal customPrice
    ) {
        LocalDate today = LocalDate.now();
        Integer userId = request.getUser().getUserID();
        Subscription plan = request.getSubscription();

        // 1) Zjistit, zda existuje aktivní a platné subscription pro daného uživatele.
        UserSubscription currentActive = findActiveValidSubscription(userId);

        if (currentActive != null) {
            // 2) Pokud uživatel má aktivní předplatné, prodloužíme ho nebo nastavíme customEndDate.
            //    a) z původního endDate (nebo dnešního data, pokud endDate chybí) vypočteme newEnd
            LocalDate oldEnd = Optional.ofNullable(currentActive.getEndDate()).orElse(today);
            LocalDate newEnd = (customEndDate != null)
                    ? customEndDate
                    : oldEnd.plusMonths(plan.getDurationMonths());
            currentActive.setEndDate(newEnd);

            // 3) Vytvoření a uložení transakce:
            //    - amount = customPrice (pokud zadané) nebo standardní cena plánu
            //    - description se liší, pokud je customEndDate
            TransactionHistory tx = new TransactionHistory();
            tx.setUser(currentActive.getUser());
            tx.setUserSubscription(currentActive);
            tx.setPurchaseType("Subscription");
            tx.setAmount((customPrice != null) ? customPrice : plan.getPrice());
            if (customEndDate != null) {
                tx.setDescription("Manuální nastavení platnosti do " + newEnd);
            } else {
                tx.setDescription("Prodloužení o " + plan.getDurationMonths() + " měsíců");
            }
            transactionHistoryRepository.save(tx);

            // 4) Uložení aktualizovaného předplatného
            return userSubscriptionRepository.save(currentActive);
        }

        // 5) Pokud neexistuje žádné platné předplatné:
        //    a) deaktivujeme všechna dosud aktivní
        deactivateActiveSubscription(userId);

        //    b) vytvoříme nové s startDate = dnes a endDate = customEndDate nebo dnes + durationMonths
        UserSubscription fresh = new UserSubscription();
        fresh.setUser(request.getUser());
        fresh.setSubscription(plan);
        fresh.setStartDate(today);
        LocalDate endDate = (customEndDate != null)
                ? customEndDate
                : today.plusMonths(plan.getDurationMonths());
        fresh.setEndDate(endDate);
        fresh.setIsActive(true);

        UserSubscription saved = userSubscriptionRepository.save(fresh);

        // 6) Vytvoření a uložení transakce pro nové předplatné:
        TransactionHistory txNew = new TransactionHistory();
        txNew.setUser(saved.getUser());
        txNew.setUserSubscription(saved);
        txNew.setPurchaseType("Subscription");
        txNew.setAmount((customPrice != null) ? customPrice : plan.getPrice());
        if (customEndDate != null) {
            txNew.setDescription("Manuální nastavení platnosti do " + endDate);
        } else {
            txNew.setDescription("Nákup předplatného na " + plan.getDurationMonths() + " měsíců");
        }
        transactionHistoryRepository.save(txNew);

        return saved;
    }

    @Override
    public UserSubscription updateUserSubscription(Integer id, UserSubscription userSubscriptionDetails) {
        return userSubscriptionRepository.findById(id)
                .map(existing -> {
                    existing.setUser(userSubscriptionDetails.getUser());
                    existing.setSubscription(userSubscriptionDetails.getSubscription());
                    existing.setStartDate(userSubscriptionDetails.getStartDate());
                    existing.setEndDate(userSubscriptionDetails.getEndDate());
                    existing.setIsActive(userSubscriptionDetails.getIsActive());
                    // Další pole...
                    return userSubscriptionRepository.save(existing);
                }).orElseThrow(() ->
                        new ResourceNotFoundException("UserSubscription not found with id " + id));
    }

    @Override
    public void deleteUserSubscription(Integer id) {
        UserSubscription sub = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found with id " + id));
        userSubscriptionRepository.delete(sub);
    }

    @Override
    public List<UserSubscription> findByUserId(Integer userId) {
        return userSubscriptionRepository.findByUserUserID(userId);
    }

    @Override
    public List<UserSubscription> findActiveSubscriptions() {
        return userSubscriptionRepository.findByIsActiveTrue();
    }

    // -------------------------------------------------------------------
    // POMOCNÉ METODY
    // -------------------------------------------------------------------

    /**
     * Najde aktivní a platné (tj. endDate > dnes) předplatné daného uživatele.
     * Pokud existuje více, vezmeme to první nalezené.
     */
    private UserSubscription findActiveValidSubscription(Integer userId) {
        LocalDate today = LocalDate.now();
        return userSubscriptionRepository.findByUserUserID(userId).stream()
                .filter(us -> Boolean.TRUE.equals(us.getIsActive()))
                .filter(us -> us.getEndDate() != null && us.getEndDate().isAfter(today))
                .findFirst()
                .orElse(null);
    }

    /**
     * Všechny aktivní subscription (pokud existují) nastaví na isActive = false.
     * Pro situaci, kdy chceme vytvořit nové a nechceme “dvojité” aktivní subscription.
     */
    private void deactivateActiveSubscription(Integer userId) {
        List<UserSubscription> activeSubs = userSubscriptionRepository.findByUserUserID(userId).stream()
                .filter(us -> Boolean.TRUE.equals(us.getIsActive()))
                .toList();

        for (UserSubscription sub : activeSubs) {
            sub.setIsActive(false);
            userSubscriptionRepository.save(sub);
        }
    }

    /**
     * Prodlouží stávající subscription o daný počet měsíců.
     * startDate zůstává, endDate = endDate + durationMonths
     */
    private void extendSubscription(UserSubscription active, int durationMonths) {
        LocalDate oldEnd = active.getEndDate();
        if (oldEnd == null) {
            oldEnd = LocalDate.now();
        }
        active.setEndDate(oldEnd.plusMonths(durationMonths));
    }

    /**
     * Vytvoří novou subscription s platností od dneška na X měsíců.
     * isActive = true, apod.
     */
    private UserSubscription createNewSubscription(UserSubscription request) {
        UserSubscription newSub = new UserSubscription();
        newSub.setUser(request.getUser());
        newSub.setSubscription(request.getSubscription());
        newSub.setIsActive(true);

        LocalDate today = LocalDate.now();
        newSub.setStartDate(today);
        // endDate = dnes + subscription X měsíců
        int months = request.getSubscription().getDurationMonths();
        newSub.setEndDate(today.plusMonths(months));

        return newSub;
    }

    /**
     * Vytvoří záznam do TransactionHistory (purchaseType, amount, description).
     * purchaseType = subscription type (např. “Monthly”, “3 months” atd.),
     * amount = subscription.getPrice(), atd.
     */
    private void createTransactionHistory(UserSubscription userSubscription, Subscription subPlan) {
        TransactionHistory tx = new TransactionHistory();
        tx.setUser(userSubscription.getUser());
        tx.setUserSubscription(userSubscription);

        tx.setPurchaseType("Subscription");
        tx.setAmount(subPlan.getPrice());
        tx.setDescription("Nákup/Prodloužení předplatného: " + subPlan.getSubscriptionType());

        // transactionDate se může nastavovat automaticky v entitě
        transactionHistoryRepository.save(tx);
    }
}