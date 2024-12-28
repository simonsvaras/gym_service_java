package com.gym.gymmanagementsystem.controllers;


import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.UserOneTimeEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-one-time-entries")
public class UserOneTimeEntryController {

    private final UserOneTimeEntryService userOneTimeEntryService;

    public UserOneTimeEntryController(UserOneTimeEntryService userOneTimeEntryService) {
        this.userOneTimeEntryService = userOneTimeEntryService;
    }

    // GET /api/user-one-time-entries
    @GetMapping
    public ResponseEntity<List<UserOneTimeEntry>> getAllUserOneTimeEntries() {
        List<UserOneTimeEntry> entries = userOneTimeEntryService.getAllUserOneTimeEntries();
        return ResponseEntity.ok(entries);
    }

    // GET /api/user-one-time-entries/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserOneTimeEntry> getUserOneTimeEntryById(@PathVariable Integer id) {
        UserOneTimeEntry entry = userOneTimeEntryService.getUserOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry not found with id " + id));
        return ResponseEntity.ok(entry);
    }

    // POST /api/user-one-time-entries
    @PostMapping
    public ResponseEntity<UserOneTimeEntry> createUserOneTimeEntry(@RequestBody UserOneTimeEntry userOneTimeEntry) {
        UserOneTimeEntry created = userOneTimeEntryService.createUserOneTimeEntry(userOneTimeEntry);
        return ResponseEntity.ok(created);
    }

    // PUT /api/user-one-time-entries/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserOneTimeEntry> updateUserOneTimeEntry(@PathVariable Integer id,
                                                                   @RequestBody UserOneTimeEntry entryDetails) {
        UserOneTimeEntry updated = userOneTimeEntryService.updateUserOneTimeEntry(id, entryDetails);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/user-one-time-entries/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserOneTimeEntry(@PathVariable Integer id) {
        userOneTimeEntryService.deleteUserOneTimeEntry(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/user-one-time-entries/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserOneTimeEntry>> findByUserId(@PathVariable Integer userId) {
        List<UserOneTimeEntry> entries = userOneTimeEntryService.findByUserId(userId);
        return ResponseEntity.ok(entries);
    }

    // Speciální příklad: GET /api/user-one-time-entries/unused
    @GetMapping("/unused")
    public ResponseEntity<List<UserOneTimeEntry>> findUnusedEntries() {
        List<UserOneTimeEntry> unused = userOneTimeEntryService.findUnusedEntries();
        return ResponseEntity.ok(unused);
    }
}
