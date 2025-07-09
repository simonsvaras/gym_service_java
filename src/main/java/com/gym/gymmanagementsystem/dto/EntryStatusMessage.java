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

    private String firstname;

    private String lastname;

    private String userId;

    private String status;

    private Integer remainingEntries;

    private LocalDate expiryDate;

    /** Motivational text shown with the entry result. */
    private String text;
}
