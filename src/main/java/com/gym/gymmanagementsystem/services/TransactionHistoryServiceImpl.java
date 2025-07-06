package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private static final Logger log = LoggerFactory.getLogger(TransactionHistoryServiceImpl.class);

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public List<TransactionHistory> getAllTransactionHistories() {
        log.info("Načítám historii transakcí");
        List<TransactionHistory> list = transactionHistoryRepository.findAll();
        log.debug("Nalezeno {} transakcí", list.size());
        return list;
    }

    @Override
    public Optional<TransactionHistory> getTransactionHistoryById(Integer id) {
        log.info("Hledám transakci id={}", id);
        return transactionHistoryRepository.findById(id);
    }

    @Override
    public TransactionHistory createTransactionHistory(TransactionHistory transactionHistory) {
        log.info("Vytvářím transakci: {}", transactionHistory);
        TransactionHistory saved = transactionHistoryRepository.save(transactionHistory);
        log.debug("Transakce uložena s ID {}", saved.getTransactionID());
        return saved;
    }

    @Override
    public TransactionHistory updateTransactionHistory(Integer id, TransactionHistory transactionHistoryDetails) {
        log.info("Aktualizuji transakci id={}", id);
        return transactionHistoryRepository.findById(id)
                .map(transactionHistory -> {
                    transactionHistory.setUser(transactionHistoryDetails.getUser());
                    transactionHistory.setTransactionDate(transactionHistoryDetails.getTransactionDate());
                    transactionHistory.setAmount(transactionHistoryDetails.getAmount());
                    transactionHistory.setDescription(transactionHistoryDetails.getDescription());
                    transactionHistory.setPurchaseType(transactionHistoryDetails.getPurchaseType());
                    transactionHistory.setUserSubscription(transactionHistoryDetails.getUserSubscription());
                    transactionHistory.setOneTimeEntry(transactionHistoryDetails.getOneTimeEntry());
                    // Aktualizujte další pole podle potřeby
                    TransactionHistory t = transactionHistoryRepository.save(transactionHistory);
                    log.debug("Transakce {} aktualizována", id);
                    return t;
                }).orElseThrow(() -> new ResourceNotFoundException("TransactionHistory not found with id " + id));
    }

    @Override
    public void deleteTransactionHistory(Integer id) {
        log.info("Mažu transakci id={}", id);
        TransactionHistory transactionHistory = transactionHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransactionHistory not found with id " + id));
        transactionHistoryRepository.delete(transactionHistory);
        log.debug("Transakce {} smazána", id);
    }

    @Override
    public List<TransactionHistory> findByUserId(Integer userId) {
        log.info("Hledám transakce pro uživatele {}", userId);
        return transactionHistoryRepository.findByUserUserID(userId);
    }

    @Override
    public List<TransactionHistory> findByPurchaseType(String purchaseType) {
        log.info("Hledám transakce typu {}", purchaseType);
        return transactionHistoryRepository.findByPurchaseType(purchaseType);
    }

    @Override
    public List<TransactionHistory> getTransactionsInRange(LocalDateTime start, LocalDateTime end) {
        log.info("Hledám transakce v období {} - {}", start, end);
        return transactionHistoryRepository.findByTransactionDateBetween(start, end);
    }
}
