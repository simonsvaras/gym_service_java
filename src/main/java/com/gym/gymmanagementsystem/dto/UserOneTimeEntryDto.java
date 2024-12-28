package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserOneTimeEntryDto {

    private Integer userOneTimeEntryID;

    @NotNull(message = "User ID je povinný")
    private Integer userID;

    @NotNull(message = "OneTimeEntry ID je povinný")
    private Integer oneTimeEntryID;

    private LocalDate purchaseDate;

    private Boolean isUsed = false;

    // Můžeš přidat další pole nebo vztahy podle potřeby
}
