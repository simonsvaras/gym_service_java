package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.EntryHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntryHistoryServiceImpl implements EntryHistoryService {

    @Autowired
    private EntryHistoryRepository entryHistoryRepository;

    @Override
    public List<EntryHistory> getAllEntryHistories() {
        return entryHistoryRepository.findAll();
    }

    @Override
    public Optional<EntryHistory> getEntryHistoryById(Integer id) {
        return entryHistoryRepository.findById(id);
    }

    @Override
    public EntryHistory createEntryHistory(EntryHistory entryHistory) {
        // Případná validace nebo další logika
        return entryHistoryRepository.save(entryHistory);
    }

    @Override
    public EntryHistory updateEntryHistory(Integer id, EntryHistory entryHistoryDetails) {
        return entryHistoryRepository.findById(id)
                .map(entryHistory -> {
                    entryHistory.setUser(entryHistoryDetails.getUser());
                    entryHistory.setEntryDate(entryHistoryDetails.getEntryDate());
                    entryHistory.setEntryType(entryHistoryDetails.getEntryType());
                    // Aktualizujte další pole podle potřeby
                    return entryHistoryRepository.save(entryHistory);
                }).orElseThrow(() -> new ResourceNotFoundException("EntryHistory not found with id " + id));
    }

    @Override
    public void deleteEntryHistory(Integer id) {
        EntryHistory entryHistory = entryHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntryHistory not found with id " + id));
        entryHistoryRepository.delete(entryHistory);
    }

    @Override
    public List<EntryHistory> findByUserId(Integer userId) {
        return entryHistoryRepository.findByUserUserID(userId);
    }

    @Override
    public List<EntryHistory> getEntriesInRange(LocalDateTime start, LocalDateTime end){
        return entryHistoryRepository.findByEntryDateBetween(start, end);
    }


}
