package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserOneTimeEntryRepository;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import com.gym.gymmanagementsystem.repositories.OneTimeEntryRepository;
import com.gym.gymmanagementsystem.dto.UserOneTimeEntryDto;
import com.gym.gymmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
public class UserOneTimeEntryServiceImpl implements UserOneTimeEntryService {

    private static final Logger log = LoggerFactory.getLogger(UserOneTimeEntryServiceImpl.class);

    @Autowired
    private UserOneTimeEntryRepository userOneTimeEntryRepository;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OneTimeEntryRepository oneTimeEntryRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<UserOneTimeEntry> getAllUserOneTimeEntries() {
        log.info("Načítám jednorázové vstupy uživatelů");
        List<UserOneTimeEntry> list = userOneTimeEntryRepository.findAll();
        log.debug("Nalezeno {} záznamů", list.size());
        return list;
    }

    @Override
    public Optional<UserOneTimeEntry> getUserOneTimeEntryById(Integer id) {
        log.info("Hledám jednorázový vstup uživatele id={}", id);
        return userOneTimeEntryRepository.findById(id);
    }

    @Override
    public UserOneTimeEntry createUserOneTimeEntry(UserOneTimeEntry userOneTimeEntry, BigDecimal customPrice) {
        log.info("Vytvářím jednorázový vstup uživatele: {} customPrice={}", userOneTimeEntry, customPrice);
        UserOneTimeEntry createdEntry = userOneTimeEntryRepository.save(userOneTimeEntry);

        // Vytvoření a uložení záznamu transakce
        TransactionHistory transaction = new TransactionHistory();
        transaction.setUser(createdEntry.getUser()); // Předpokládáme, že UserOneTimeEntry má referenci na User
        transaction.setAmount(customPrice != null ? customPrice : createdEntry.getOneTimeEntry().getPrice());
        transaction.setDescription("Nákup jednorázového vstupu " + createdEntry.getOneTimeEntry().getEntryName());
        transaction.setPurchaseType(createdEntry.getOneTimeEntry().getEntryName());
        transaction.setOneTimeEntry(createdEntry);


        transactionHistoryService.createTransactionHistory(transaction);

        log.debug("Jednorázový vstup uložen s ID {}", createdEntry.getUserOneTimeEntryID());
        return createdEntry;
    }

    @Override
    public UserOneTimeEntry updateUserOneTimeEntry(Integer id, UserOneTimeEntry userOneTimeEntryDetails) {
        log.info("Aktualizuji jednorázový vstup uživatele id={}", id);
        return userOneTimeEntryRepository.findById(id)
                .map(userOneTimeEntry -> {
                    userOneTimeEntry.setUser(userOneTimeEntryDetails.getUser());
                    userOneTimeEntry.setOneTimeEntry(userOneTimeEntryDetails.getOneTimeEntry());
                    userOneTimeEntry.setPurchaseDate(userOneTimeEntryDetails.getPurchaseDate());
                    userOneTimeEntry.setIsUsed(userOneTimeEntryDetails.getIsUsed());
                    // Aktualizujte další pole podle potřeby
                    UserOneTimeEntry e = userOneTimeEntryRepository.save(userOneTimeEntry);
                    log.debug("Jednorázový vstup {} aktualizován", id);
                    return e;
                }).orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry not found with id " + id));
    }

    @Override
    public void deleteUserOneTimeEntry(Integer id) {
        log.info("Mažu jednorázový vstup uživatele id={}", id);
        UserOneTimeEntry userOneTimeEntry = userOneTimeEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry not found with id " + id));
        userOneTimeEntryRepository.delete(userOneTimeEntry);
        log.debug("Jednorázový vstup {} smazán", id);
    }

    @Override
    public List<UserOneTimeEntry> findByUserId(Integer userId) {
        log.info("Hledám jednorázové vstupy pro uživatele {}", userId);
        return userOneTimeEntryRepository.findByUserUserID(userId);
    }

    @Override
    public List<UserOneTimeEntry> findUnusedEntries() {
        log.info("Hledám nevyužité jednorázové vstupy");
        return userOneTimeEntryRepository.findByIsUsedFalse();
    }

    @Override
    public List<UserOneTimeEntry> createEntriesForUnregistered(UserOneTimeEntryDto dto, int count) {
        log.info("Vytvářím jednorázové vstupy pro neregistrovaného uživatele count={}", count);

        // 1) najdeme volného falešného uživatele bez platného vstupu
        User target = null;
        for (User u : userRepository.findByRealUserFalse()) {
            if (userOneTimeEntryRepository.countByUserUserIDAndIsUsedFalse(u.getUserID()) == 0) {
                target = u;
                break;
            }
        }

        if (target == null) {
            User newUser = new User();
            newUser.setFirstname("Guest");
            newUser.setLastname("User");
            newUser.setEmail("guest" + UUID.randomUUID() + "@example.com");
            newUser.setRealUser(false);
            target = userRepository.save(newUser);
        }

        // přiřazení karty
        userService.assignCardToUser(target.getUserID(), dto.getCardNumber());

        OneTimeEntry oneTime = oneTimeEntryRepository.findById(dto.getOneTimeEntryID())
                .orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry not found with id " + dto.getOneTimeEntryID()));

        List<UserOneTimeEntry> created = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserOneTimeEntry entry = new UserOneTimeEntry();
            entry.setUser(target);
            entry.setOneTimeEntry(oneTime);
            entry.setPurchaseDate(dto.getPurchaseDate());
            entry.setIsUsed(false);
            created.add(createUserOneTimeEntry(entry, dto.getCustomPrice()));
        }

        return created;
    }
}
