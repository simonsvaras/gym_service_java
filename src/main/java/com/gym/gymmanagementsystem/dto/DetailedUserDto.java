package com.gym.gymmanagementsystem.dto;

import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DetailedUserDto {

    private Integer userID;
    private String firstname;
    private String lastname;
    private String email;
    // Základní URL pro stažení profilové fotky. Kvalitu lze určit pomocí parametru "quality".
    private String profilePhotoPath;

    private Integer points;

    private List<UserSubscriptionDto> subscriptions;

    private List<EntryHistoryDto> entryHistories;
}