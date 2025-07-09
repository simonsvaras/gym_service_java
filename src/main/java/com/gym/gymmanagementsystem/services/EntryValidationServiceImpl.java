package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.dto.EntryStatusMessage;
import com.gym.gymmanagementsystem.dto.enums.MotivationalMessage;
import com.gym.gymmanagementsystem.repositories.UserOneTimeEntryRepository;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import com.gym.gymmanagementsystem.repositories.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
            EntryStatusMessage msg = new EntryStatusMessage();
            msg.setUserId(String.valueOf(userId));
            msg.setFirstname(user.getFirstname());
            msg.setLastname(user.getLastname());
            msg.setStatus("OK_SUBSCRIPTION");
            msg.setExpiryDate(active.getEndDate());
            msg.setText(MotivationalMessage.randomText());
            notifyEntryStatus(msg);
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
            long remaining = userOneTimeEntryRepository.findByUserUserID(userId)
                    .stream()
                    .filter(e -> !Boolean.TRUE.equals(e.getIsUsed()))
                    .count();
            EntryStatusMessage msg = new EntryStatusMessage();
            msg.setFirstname(user.getFirstname());
            msg.setLastname(user.getLastname());
            msg.setUserId(String.valueOf(userId));
            msg.setStatus("OK_ONE_TIME_ENTRY");
            msg.setRemainingEntries((int) remaining);
            msg.setText(MotivationalMessage.randomText());
            notifyEntryStatus(msg);
            return new EntryValidationResult(true, "OneTimeEntry");
        }

        EntryStatusMessage msg = new EntryStatusMessage();
        msg.setUserId(String.valueOf(userId));
        msg.setFirstname(user.getFirstname());
        msg.setLastname(user.getLastname());
        msg.setStatus("NO_VALID_ENTRY");
        msg.setText(MotivationalMessage.randomText());
        notifyEntryStatus(msg);
        return new EntryValidationResult(false, null);
    }

    private void notifyEntryStatus(EntryStatusMessage message) {
        simpMessagingTemplate.convertAndSend("/topic/entry-status", message);
    }
}
