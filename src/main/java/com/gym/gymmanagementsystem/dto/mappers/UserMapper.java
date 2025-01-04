package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.UserDto;
import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.repositories.CardRepository;
import com.gym.gymmanagementsystem.repositories.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserSubscriptionRepository subscriptionRepository;

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setBirthdate(userDto.getBirthdate());
        user.setProfilePhoto(userDto.getProfilePhoto());
        user.setRealUser(userDto.getRealUser());

        if (userDto.getCardID() != null) {
            Card card = cardRepository.findById(userDto.getCardID()).orElse(null);
            user.setCard(card);
        }

        if (userDto.getActiveSubscriptionID() != null) {
            UserSubscription subscription = subscriptionRepository.findById(userDto.getActiveSubscriptionID()).orElse(null);
            user.setActiveSubscription(subscription);
        }

        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setUserID(user.getUserID());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setBirthdate(user.getBirthdate());
        dto.setProfilePhoto(user.getProfilePhoto());
        dto.setRealUser(user.getRealUser());

        if (user.getCard() != null) {
            dto.setCardID(user.getCard().getCardID());
        }

        if (user.getActiveSubscription() != null) {
            dto.setActiveSubscriptionID(user.getActiveSubscription().getUserSubscriptionID());
        }

        return dto;
    }
}
