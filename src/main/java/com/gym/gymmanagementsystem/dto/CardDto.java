package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardDto {

    private Integer cardID;

    @NotBlank(message = "Card number je povinný")
    @Size(max = 50, message = "Card number může mít maximálně 50 znaků")
    private String cardNumber;

    private Boolean lost;

    @Size(max = 50, message = "Card type může mít maximálně 50 znaků")
    private String cardType;

    // Pro vztahy můžeš použít ID nebo další DTO podle potřeby
}
