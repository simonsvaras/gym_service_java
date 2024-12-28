package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OneTimeEntryDto {

    private Integer oneTimeEntryID;

    @NotBlank(message = "Entry name je povinný")
    @Size(max = 50, message = "Entry name může mít maximálně 50 znaků")
    private String entryName;

    @NotNull(message = "Price je povinný")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price musí být kladná hodnota")
    private BigDecimal price;
}
