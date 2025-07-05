package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.TransactionHistoryDto;
import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.repositories.UserOneTimeEntryRepository;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import com.gym.gymmanagementsystem.repositories.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionHistoryMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private UserOneTimeEntryRepository userOneTimeEntryRepository;

    // Převod z TransactionHistoryDto na TransactionHistory entitu
    public TransactionHistory toEntity(TransactionHistoryDto dto) {
        if (dto == null) {
            return null;
        }

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionID(dto.getTransactionID());

        if (dto.getUserID() != null) {
            User user = userRepository.findById(dto.getUserID()).orElse(null);
            transactionHistory.setUser(user);
        }

        transactionHistory.setTransactionDate(dto.getTransactionDate());
        transactionHistory.setAmount(dto.getAmount());
        transactionHistory.setDescription(dto.getDescription());
        transactionHistory.setPurchaseType(dto.getPurchaseType());

        if (dto.getUserSubscriptionID() != null) {
            UserSubscription userSubscription = userSubscriptionRepository.findById(dto.getUserSubscriptionID()).orElse(null);
            transactionHistory.setUserSubscription(userSubscription);
        }

        if (dto.getOneTimeEntryID() != null) {
            UserOneTimeEntry oneTimeEntry = userOneTimeEntryRepository.findById(dto.getOneTimeEntryID()).orElse(null);
            transactionHistory.setOneTimeEntry(oneTimeEntry);
        }

        return transactionHistory;
    }

    // Převod z TransactionHistory entity na TransactionHistoryDto
    public TransactionHistoryDto toDto(TransactionHistory transactionHistory) {
        if (transactionHistory == null) {
            return null;
        }

        TransactionHistoryDto dto = new TransactionHistoryDto();
        dto.setTransactionID(transactionHistory.getTransactionID());

        if (transactionHistory.getUser() != null) {
            dto.setUserID(transactionHistory.getUser().getUserID());
            dto.setFirstName(transactionHistory.getUser().getFirstname());
            dto.setLastName(transactionHistory.getUser().getLastname());

        }

        dto.setTransactionDate(transactionHistory.getTransactionDate());
        dto.setAmount(transactionHistory.getAmount());
        dto.setDescription(transactionHistory.getDescription());
        dto.setPurchaseType(transactionHistory.getPurchaseType());

        if (transactionHistory.getUserSubscription() != null) {
            dto.setUserSubscriptionID(transactionHistory.getUserSubscription().getUserSubscriptionID());
        }

        if (transactionHistory.getOneTimeEntry() != null) {
            dto.setOneTimeEntryID(transactionHistory.getOneTimeEntry().getUserOneTimeEntryID());
        }

        return dto;
    }
}
