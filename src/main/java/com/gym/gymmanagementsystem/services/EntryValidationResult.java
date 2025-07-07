package com.gym.gymmanagementsystem.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Výsledek ověření vstupu do gymu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryValidationResult {
    /** zda je vstup povolen */
    private boolean allowed;
    /** typ vstupu: Subscription nebo OneTimeEntry */
    private String type;
}
