package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserOneTimeEntryRepository;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import com.gym.gymmanagementsystem.repositories.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementace služby pro ověření vstupu.
 */
@Service
@Transactional
public class EntryValidationServiceImpl implements EntryValidationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private UserOneTimeEntryRepository userOneTimeEntryRepository;
    @Autowired
    private EntryHistoryService entryHistoryService;

    @Override
    public EntryValidationResult canUserEnter(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        LocalDate today = LocalDate.now();
        // kontrola aktivního předplatného
        UserSubscription active = userSubscriptionRepository.findByUserUserID(userId).stream()
                .filter(us -> Boolean.TRUE.equals(us.getIsActive()))
                .filter(us -> us.getEndDate() != null && (us.getEndDate().isAfter(today) || us.getEndDate().isEqual(today)))
                .findFirst()
                .orElse(null);
        if (active != null) {
            EntryHistory history = new EntryHistory();
            history.setUser(user);
            history.setEntryType("Subscription");
            entryHistoryService.createEntryHistory(history);
            return new EntryValidationResult(true, "Subscription");
        }

        // kontrola jednorázového vstupu
        UserOneTimeEntry oneTime = userOneTimeEntryRepository.findByUserUserID(userId).stream()
                .filter(e -> !Boolean.TRUE.equals(e.getIsUsed()))
                .findFirst()
                .orElse(null);
        if (oneTime != null) {
            oneTime.setIsUsed(true);
            userOneTimeEntryRepository.save(oneTime);

            EntryHistory history = new EntryHistory();
            history.setUser(user);
            history.setEntryType("OneTimeEntry");
            entryHistoryService.createEntryHistory(history);
            return new EntryValidationResult(true, "OneTimeEntry");
        }

        return new EntryValidationResult(false, null);
    }
}
