package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardDto {

    private Integer cardID;

    @NotNull(message = "Card number je povinný")
    private Long cardNumber;

    private Boolean lost;

    @Size(max = 50, message = "Card type může mít maximálně 50 znaků")
    private String cardType;

    // Pro vztahy můžeš použít ID nebo další DTO podle potřeby
}
