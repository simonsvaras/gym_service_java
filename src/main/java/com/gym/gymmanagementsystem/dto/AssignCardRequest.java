package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request object for assigning a card to a user.
 */
@Data
public class AssignCardRequest {
    @NotBlank(message = "Card number je povinný")
    private String cardNumber;
}
