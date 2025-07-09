package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.dto.UserOneTimeEntryDto;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

public interface UserOneTimeEntryService {
    List<UserOneTimeEntry> getAllUserOneTimeEntries();
    Optional<UserOneTimeEntry> getUserOneTimeEntryById(Integer id);
    UserOneTimeEntry createUserOneTimeEntry(UserOneTimeEntry userOneTimeEntry, BigDecimal customPrice);
    UserOneTimeEntry updateUserOneTimeEntry(Integer id, UserOneTimeEntry userOneTimeEntryDetails);
    void deleteUserOneTimeEntry(Integer id);
    List<UserOneTimeEntry> findByUserId(Integer userId);
    List<UserOneTimeEntry> findUnusedEntries();

    /**
     * Vytvoří jednorázové vstupy pro neregistrovaného uživatele.
     *
     * @param dto   informace o vstupu včetně cardNumber
     * @param count počet vstupů
     * @return seznam vytvořených vstupů
     */
    List<UserOneTimeEntry> createEntriesForUnregistered(UserOneTimeEntryDto dto, int count);
}
