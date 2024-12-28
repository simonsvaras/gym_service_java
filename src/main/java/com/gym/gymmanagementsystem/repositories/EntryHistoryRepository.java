package com.gym.gymmanagementsystem.repositories;

import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.entities.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryHistoryRepository extends JpaRepository<EntryHistory, Integer> {
    List<EntryHistory> findByUserUserID(Integer userID);


}
