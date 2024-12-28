package com.gym.gymmanagementsystem.controllers;


import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.OneTimeEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/one-time-entries")
public class OneTimeEntryController {

    private final OneTimeEntryService oneTimeEntryService;

    public OneTimeEntryController(OneTimeEntryService oneTimeEntryService) {
        this.oneTimeEntryService = oneTimeEntryService;
    }

    // GET /api/one-time-entries
    @GetMapping
    public ResponseEntity<List<OneTimeEntry>> getAllOneTimeEntries() {
        List<OneTimeEntry> entries = oneTimeEntryService.getAllOneTimeEntries();
        return ResponseEntity.ok(entries);
    }

    // GET /api/one-time-entries/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OneTimeEntry> getOneTimeEntryById(@PathVariable Integer id) {
        OneTimeEntry entry = oneTimeEntryService.getOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry not found with id " + id));
        return ResponseEntity.ok(entry);
    }

    // POST /api/one-time-entries
    @PostMapping
    public ResponseEntity<OneTimeEntry> createOneTimeEntry(@RequestBody OneTimeEntry oneTimeEntry) {
        OneTimeEntry createdEntry = oneTimeEntryService.createOneTimeEntry(oneTimeEntry);
        return ResponseEntity.ok(createdEntry);
    }

    // PUT /api/one-time-entries/{id}
    @PutMapping("/{id}")
    public ResponseEntity<OneTimeEntry> updateOneTimeEntry(@PathVariable Integer id,
                                                           @RequestBody OneTimeEntry oneTimeEntryDetails) {
        OneTimeEntry updatedEntry = oneTimeEntryService.updateOneTimeEntry(id, oneTimeEntryDetails);
        return ResponseEntity.ok(updatedEntry);
    }

    // DELETE /api/one-time-entries/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOneTimeEntry(@PathVariable Integer id) {
        oneTimeEntryService.deleteOneTimeEntry(id);
        return ResponseEntity.noContent().build();
    }
}
