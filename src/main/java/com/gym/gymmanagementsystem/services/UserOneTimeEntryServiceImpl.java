package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserOneTimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserOneTimeEntryServiceImpl implements UserOneTimeEntryService {

    @Autowired
    private UserOneTimeEntryRepository userOneTimeEntryRepository;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Override
    public List<UserOneTimeEntry> getAllUserOneTimeEntries() {
        return userOneTimeEntryRepository.findAll();
    }

    @Override
    public Optional<UserOneTimeEntry> getUserOneTimeEntryById(Integer id) {
        return userOneTimeEntryRepository.findById(id);
    }

    @Override
    public UserOneTimeEntry createUserOneTimeEntry(UserOneTimeEntry userOneTimeEntry) {
        // Uložení nového jednorázového předplatného
        UserOneTimeEntry createdEntry = userOneTimeEntryRepository.save(userOneTimeEntry);

        // Vytvoření a uložení záznamu transakce
        TransactionHistory transaction = new TransactionHistory();
        transaction.setUser(createdEntry.getUser()); // Předpokládáme, že UserOneTimeEntry má referenci na User
        transaction.setAmount(createdEntry.getOneTimeEntry().getPrice()); // Předpokládáme, že OneTimeEntry má cenu
        transaction.setDescription("Nákup jednorázového vstupu " + createdEntry.getOneTimeEntry().getEntryName());
        transaction.setPurchaseType(createdEntry.getOneTimeEntry().getEntryName());
        transaction.setOneTimeEntry(createdEntry);


        transactionHistoryService.createTransactionHistory(transaction);

        return createdEntry;
    }

    @Override
    public UserOneTimeEntry updateUserOneTimeEntry(Integer id, UserOneTimeEntry userOneTimeEntryDetails) {
        return userOneTimeEntryRepository.findById(id)
                .map(userOneTimeEntry -> {
                    userOneTimeEntry.setUser(userOneTimeEntryDetails.getUser());
                    userOneTimeEntry.setOneTimeEntry(userOneTimeEntryDetails.getOneTimeEntry());
                    userOneTimeEntry.setPurchaseDate(userOneTimeEntryDetails.getPurchaseDate());
                    userOneTimeEntry.setIsUsed(userOneTimeEntryDetails.getIsUsed());
                    // Aktualizujte další pole podle potřeby
                    return userOneTimeEntryRepository.save(userOneTimeEntry);
                }).orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry not found with id " + id));
    }

    @Override
    public void deleteUserOneTimeEntry(Integer id) {
        UserOneTimeEntry userOneTimeEntry = userOneTimeEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry not found with id " + id));
        userOneTimeEntryRepository.delete(userOneTimeEntry);
    }

    @Override
    public List<UserOneTimeEntry> findByUserId(Integer userId) {
        return userOneTimeEntryRepository.findByUserUserID(userId);
    }

    @Override
    public List<UserOneTimeEntry> findUnusedEntries() {
        return userOneTimeEntryRepository.findByIsUsedFalse();
    }
}
