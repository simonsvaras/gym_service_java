package com.gym.gymmanagementsystem.services;

/**
 * Služba pro ověření, zda má uživatel právo vstoupit do gymu.
 */
public interface EntryValidationService {
    /**
     * Ověří, zda uživatel může vstoupit do gymu.
     * @param userId id uživatele
     * @return výsledek ověření
     */
    EntryValidationResult canUserEnter(Integer userId);
}
