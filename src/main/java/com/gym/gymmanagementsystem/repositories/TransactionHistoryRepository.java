package com.gym.gymmanagementsystem.repositories;

import com.gym.gymmanagementsystem.entities.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {
    List<TransactionHistory> findByUserUserID(Integer userID);
    List<TransactionHistory> findByPurchaseType(String purchaseType);
}