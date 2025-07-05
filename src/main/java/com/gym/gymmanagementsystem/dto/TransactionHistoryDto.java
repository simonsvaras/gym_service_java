package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionHistoryDto {

    private Integer transactionID;

    @NotNull(message = "User ID je povinný")
    private Integer userID;

    private LocalDateTime transactionDate;

    @NotNull(message = "Amount je povinný")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount musí být kladná hodnota")
    private BigDecimal amount;

    @Size(max = 255, message = "Description může mít maximálně 255 znaků")
    private String description;

    @Size(max = 50, message = "Purchase type může mít maximálně 50 znaků")
    private String purchaseType; // 'Subscription' nebo 'OneTimeEntry'

    private Integer userSubscriptionID;
    private Integer oneTimeEntryID;

    private String lastName;
    private String firstName;
}
