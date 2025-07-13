package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request object for assigning a card to a user.
 */
@Data
public class AssignCardRequest {
    @NotNull(message = "Card number je povinný")
    private Long cardNumber;
}
