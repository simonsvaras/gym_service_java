package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserOneTimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserOneTimeEntryServiceImpl implements UserOneTimeEntryService {

    private static final Logger log = LoggerFactory.getLogger(UserOneTimeEntryServiceImpl.class);

    @Autowired
    private UserOneTimeEntryRepository userOneTimeEntryRepository;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

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
    public UserOneTimeEntry createUserOneTimeEntry(UserOneTimeEntry userOneTimeEntry) {
        log.info("Vytvářím jednorázový vstup uživatele: {}", userOneTimeEntry);
        UserOneTimeEntry createdEntry = userOneTimeEntryRepository.save(userOneTimeEntry);
        
        // Vytvoření a uložení záznamu transakce
        TransactionHistory transaction = new TransactionHistory();
        transaction.setUser(createdEntry.getUser()); // Předpokládáme, že UserOneTimeEntry má referenci na User
        transaction.setAmount(createdEntry.getOneTimeEntry().getPrice()); // Předpokládáme, že OneTimeEntry má cenu
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
}
