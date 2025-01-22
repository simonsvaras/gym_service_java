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
     * - zkontroluje, zda endDate > dnes => prodlouží
     * - jinak staré nastaví isActive=false a vytvoří nové
     * - v obou případech zapíše transaction
     */
    @Override
    public UserSubscription createUserSubscription(UserSubscription request) {
        // 1) Zjistit, zda existuje aktivní a platné subscription pro daného uživatele.
        UserSubscription currentActive = findActiveValidSubscription(request.getUser().getUserID());

        // 2) Pokud je subscription stále platné (endDate > dnes), jen ho prodloužíme
        //    o request.getSubscription().getDurationMonths().
        if (currentActive != null) {
            extendSubscription(currentActive, request.getSubscription().getDurationMonths());
            // Vytvoříme transakci (price, description atd. z requestu)
            createTransactionHistory(currentActive, request.getSubscription());
            return updateUserSubscription(currentActive.getUserSubscriptionID(), currentActive);
        }

        // 3) Pokud neexistuje žádné platné subscription (nebo je expirované),
        //    staré (aktivní) předplatné (pokud existuje) deaktivujeme
        deactivateActiveSubscription(request.getUser().getUserID());

        // 4) Vytvořit nové subscription
        UserSubscription newSub = createNewSubscription(request);
        UserSubscription saved = userSubscriptionRepository.save(newSub);

        // 5) Zapsat transaction
        createTransactionHistory(saved, request.getSubscription());

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