package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.SubscriptionDto;
import com.gym.gymmanagementsystem.entities.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    // Převod z SubscriptionDto na Subscription entitu
    public Subscription toEntity(SubscriptionDto dto) {
        if (dto == null) {
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setSubscriptionID(dto.getSubscriptionID());
        subscription.setSubscriptionType(dto.getSubscriptionType());
        subscription.setDurationMonths(dto.getDurationMonths());
        subscription.setPrice(dto.getPrice());
        // userSubscriptions nejsou mapovány zde
        return subscription;
    }

    // Převod z Subscription entity na SubscriptionDto
    public SubscriptionDto toDto(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        SubscriptionDto dto = new SubscriptionDto();
        dto.setSubscriptionID(subscription.getSubscriptionID());
        dto.setSubscriptionType(subscription.getSubscriptionType());
        dto.setDurationMonths(subscription.getDurationMonths());
        dto.setPrice(subscription.getPrice());
        // userSubscriptions nejsou zahrnuty v DTO
        return dto;
    }
}
