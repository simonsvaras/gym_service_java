package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public List<TransactionHistory> getAllTransactionHistories() {
        return transactionHistoryRepository.findAll();
    }

    @Override
    public Optional<TransactionHistory> getTransactionHistoryById(Integer id) {
        return transactionHistoryRepository.findById(id);
    }

    @Override
    public TransactionHistory createTransactionHistory(TransactionHistory transactionHistory) {
        // Případná validace nebo další logika
        return transactionHistoryRepository.save(transactionHistory);
    }

    @Override
    public TransactionHistory updateTransactionHistory(Integer id, TransactionHistory transactionHistoryDetails) {
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
                    return transactionHistoryRepository.save(transactionHistory);
                }).orElseThrow(() -> new ResourceNotFoundException("TransactionHistory not found with id " + id));
    }

    @Override
    public void deleteTransactionHistory(Integer id) {
        TransactionHistory transactionHistory = transactionHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransactionHistory not found with id " + id));
        transactionHistoryRepository.delete(transactionHistory);
    }

    @Override
    public List<TransactionHistory> findByUserId(Integer userId) {
        return transactionHistoryRepository.findByUserUserID(userId);
    }

    @Override
    public List<TransactionHistory> findByPurchaseType(String purchaseType) {
        return transactionHistoryRepository.findByPurchaseType(purchaseType);
    }

    @Override
    public List<TransactionHistory> getTransactionsInRange(LocalDateTime start, LocalDateTime end) {
        return transactionHistoryRepository.findByTransactionDateBetween(start, end);
    }
}
