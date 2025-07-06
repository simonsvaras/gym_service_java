package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.EntryHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntryHistoryServiceImpl implements EntryHistoryService {

    private static final Logger log = LoggerFactory.getLogger(EntryHistoryServiceImpl.class);

    @Autowired
    private EntryHistoryRepository entryHistoryRepository;

    @Override
    public List<EntryHistory> getAllEntryHistories() {
        log.info("Načítám historii vstupů");
        List<EntryHistory> list = entryHistoryRepository.findAll();
        log.debug("Nalezeno {} záznamů", list.size());
        return list;
    }

    @Override
    public Optional<EntryHistory> getEntryHistoryById(Integer id) {
        log.info("Hledám záznam historie id={}", id);
        return entryHistoryRepository.findById(id);
    }

    @Override
    public EntryHistory createEntryHistory(EntryHistory entryHistory) {
        log.info("Vytvářím záznam historie: {}", entryHistory);
        EntryHistory saved = entryHistoryRepository.save(entryHistory);
        log.debug("Záznam uložen s ID {}", saved.getEntryID());
        return saved;
    }

    @Override
    public EntryHistory updateEntryHistory(Integer id, EntryHistory entryHistoryDetails) {
        log.info("Aktualizuji záznam historie id={}", id);
        return entryHistoryRepository.findById(id)
                .map(entryHistory -> {
                    entryHistory.setUser(entryHistoryDetails.getUser());
                    entryHistory.setEntryDate(entryHistoryDetails.getEntryDate());
                    entryHistory.setEntryType(entryHistoryDetails.getEntryType());
                    // Aktualizujte další pole podle potřeby
                    EntryHistory e = entryHistoryRepository.save(entryHistory);
                    log.debug("Záznam {} aktualizován", id);
                    return e;
                }).orElseThrow(() -> new ResourceNotFoundException("EntryHistory not found with id " + id));
    }

    @Override
    public void deleteEntryHistory(Integer id) {
        log.info("Mažu záznam historie id={}", id);
        EntryHistory entryHistory = entryHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntryHistory not found with id " + id));
        entryHistoryRepository.delete(entryHistory);
        log.debug("Záznam {} smazán", id);
    }

    @Override
    public List<EntryHistory> findByUserId(Integer userId) {
        log.info("Hledám historii pro uživatele {}", userId);
        return entryHistoryRepository.findByUserUserID(userId);
    }

    @Override
    public List<EntryHistory> getEntriesInRange(LocalDateTime start, LocalDateTime end){
        log.info("Hledám záznamy od {} do {}", start, end);
        return entryHistoryRepository.findByEntryDateBetween(start, end);
    }


}
