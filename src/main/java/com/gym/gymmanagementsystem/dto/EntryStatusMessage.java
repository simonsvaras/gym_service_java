package com.gym.gymmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

/**
 * Message sent over WebSocket containing information about the result of an entry validation.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntryStatusMessage {

    private String userId;

    /** jméno uživatele */
    private String firstname;

    /** příjmení uživatele */
    private String lastname;

    private String status;

    private Integer remainingEntries;

    private LocalDate expiryDate;
}
