package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionDto {

    private Integer subscriptionID;

    @NotBlank(message = "Subscription type je povinný")
    @Size(max = 50, message = "Subscription type může mít maximálně 50 znaků")
    private String subscriptionType;

    @NotNull(message = "Duration months je povinný")
    @Min(value = 1, message = "Duration months musí být alespoň 1 měsíc")
    private Integer durationMonths;

    @NotNull(message = "Price je povinný")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price musí být kladná hodnota")
    private BigDecimal price;

    // Můžeš přidat další pole nebo vztahy podle potřeby
}
