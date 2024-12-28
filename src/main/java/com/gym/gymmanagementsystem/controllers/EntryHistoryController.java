package com.gym.gymmanagementsystem.controllers;


import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.EntryHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entry-history")
public class EntryHistoryController {

    private final EntryHistoryService entryHistoryService;

    public EntryHistoryController(EntryHistoryService entryHistoryService) {
        this.entryHistoryService = entryHistoryService;
    }

    // GET /api/entry-history
    @GetMapping
    public ResponseEntity<List<EntryHistory>> getAllEntryHistories() {
        List<EntryHistory> histories = entryHistoryService.getAllEntryHistories();
        return ResponseEntity.ok(histories);
    }

    // GET /api/entry-history/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EntryHistory> getEntryHistoryById(@PathVariable Integer id) {
        EntryHistory history = entryHistoryService.getEntryHistoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntryHistory not found with id " + id));
        return ResponseEntity.ok(history);
    }

    // POST /api/entry-history
    @PostMapping
    public ResponseEntity<EntryHistory> createEntryHistory(@RequestBody EntryHistory entryHistory) {
        EntryHistory created = entryHistoryService.createEntryHistory(entryHistory);
        return ResponseEntity.ok(created);
    }

    // PUT /api/entry-history/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EntryHistory> updateEntryHistory(@PathVariable Integer id,
                                                           @RequestBody EntryHistory entryHistoryDetails) {
        EntryHistory updated = entryHistoryService.updateEntryHistory(id, entryHistoryDetails);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/entry-history/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntryHistory(@PathVariable Integer id) {
        entryHistoryService.deleteEntryHistory(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/entry-history/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EntryHistory>> findByUserId(@PathVariable Integer userId) {
        List<EntryHistory> results = entryHistoryService.findByUserId(userId);
        return ResponseEntity.ok(results);
    }

}
