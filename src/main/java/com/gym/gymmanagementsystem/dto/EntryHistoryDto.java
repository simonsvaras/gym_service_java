package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EntryHistoryDto {

    private Integer entryID;

    @NotNull(message = "User ID je povinný")
    private Integer userID;

    private LocalDateTime entryDate;

    @Size(max = 50, message = "Entry type může mít maximálně 50 znaků")
    private String entryType; // 'Subscription' nebo 'OneTimeEntry'

    // Můžeš přidat další pole nebo vztahy podle potřeby
}
