package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.TransactionHistory;

import java.util.List;
import java.util.Optional;

public interface TransactionHistoryService {
    List<TransactionHistory> getAllTransactionHistories();
    Optional<TransactionHistory> getTransactionHistoryById(Integer id);
    TransactionHistory createTransactionHistory(TransactionHistory transactionHistory);
    TransactionHistory updateTransactionHistory(Integer id, TransactionHistory transactionHistoryDetails);
    void deleteTransactionHistory(Integer id);
    List<TransactionHistory> findByUserId(Integer userId);
    List<TransactionHistory> findByPurchaseType(String purchaseType);
}
