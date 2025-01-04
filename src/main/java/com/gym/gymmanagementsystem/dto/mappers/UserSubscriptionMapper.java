package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.UserSubscriptionDto;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.repositories.SubscriptionRepository;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSubscriptionMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    // Převod z UserSubscriptionDto na UserSubscription entitu
    public UserSubscription toEntity(UserSubscriptionDto dto) {
        if (dto == null) {
            return null;
        }

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUserSubscriptionID(dto.getUserSubscriptionID());

        if (dto.getUserID() != null) {
            User user = userRepository.findById(dto.getUserID()).orElse(null);
            userSubscription.setUser(user);
        }

        if (dto.getSubscriptionID() != null) {
            Subscription subscription = subscriptionRepository.findById(dto.getSubscriptionID()).orElse(null);
            userSubscription.setSubscription(subscription);
        }

        userSubscription.setStartDate(dto.getStartDate());
        userSubscription.setEndDate(dto.getEndDate());
        userSubscription.setIsActive(dto.getIsActive());

        return userSubscription;
    }

    // Převod z UserSubscription entity na UserSubscriptionDto
    public UserSubscriptionDto toDto(UserSubscription userSubscription) {
        if (userSubscription == null) {
            return null;
        }

        UserSubscriptionDto dto = new UserSubscriptionDto();
        dto.setUserSubscriptionID(userSubscription.getUserSubscriptionID());

        if (userSubscription.getUser() != null) {
            dto.setUserID(userSubscription.getUser().getUserID());
        }

        if (userSubscription.getSubscription() != null) {
            dto.setSubscriptionID(userSubscription.getSubscription().getSubscriptionID());
        }

        dto.setStartDate(userSubscription.getStartDate());
        dto.setEndDate(userSubscription.getEndDate());
        dto.setIsActive(userSubscription.getIsActive());

        return dto;
    }
}
