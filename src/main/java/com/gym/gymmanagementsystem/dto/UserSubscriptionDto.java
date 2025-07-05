package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserSubscriptionDto {

    private Integer userSubscriptionID;

    @NotNull(message = "User ID je povinný")
    private Integer userID;

    @NotNull(message = "Subscription ID je povinný")
    private Integer subscriptionID;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDate customEndDate;
    protected BigDecimal customPrice;

    private Boolean isActive = false;

    // Můžeš přidat další pole nebo vztahy podle potřeby
}
