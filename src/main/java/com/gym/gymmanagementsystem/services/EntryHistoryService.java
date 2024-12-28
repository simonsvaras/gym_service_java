package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.EntryHistory;

import java.util.List;
import java.util.Optional;

public interface EntryHistoryService {
    List<EntryHistory> getAllEntryHistories();
    Optional<EntryHistory> getEntryHistoryById(Integer id);
    EntryHistory createEntryHistory(EntryHistory entryHistory);
    EntryHistory updateEntryHistory(Integer id, EntryHistory entryHistoryDetails);
    void deleteEntryHistory(Integer id);
    List<EntryHistory> findByUserId(Integer userId);
}
