package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.EntryHistoryDto;
import com.gym.gymmanagementsystem.dto.mappers.EntryHistoryMapper;
import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.EntryHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entry-history")
public class EntryHistoryController {

    private final EntryHistoryService entryHistoryService;
    private EntryHistoryMapper mapper;

    public EntryHistoryController(EntryHistoryService entryHistoryService) {
        this.entryHistoryService = entryHistoryService;
    }

    // GET /api/entry-history
    @GetMapping
    public ResponseEntity<List<EntryHistoryDto>> getAllEntryHistories() {
        List<EntryHistory> histories = entryHistoryService.getAllEntryHistories();
        List<EntryHistoryDto> historyDtos = histories.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(historyDtos);
    }

    // GET /api/entry-history/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EntryHistoryDto> getEntryHistoryById(@PathVariable Integer id) {
        EntryHistory history = entryHistoryService.getEntryHistoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntryHistory not found with id " + id));
        EntryHistoryDto dto = mapper.toDto(history);
        return ResponseEntity.ok(dto);
    }

    // POST /api/entry-history
    @PostMapping
    public ResponseEntity<EntryHistoryDto> createEntryHistory(@Valid @RequestBody EntryHistoryDto entryHistoryDto) {
        EntryHistory entryHistory = mapper.toEntity(entryHistoryDto);
        EntryHistory created = entryHistoryService.createEntryHistory(entryHistory);
        EntryHistoryDto createdDto = mapper.toDto(created);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/entry-history/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EntryHistoryDto> updateEntryHistory(@PathVariable Integer id,
                                                              @Valid @RequestBody EntryHistoryDto entryHistoryDto) {
        EntryHistory entryHistoryDetails = mapper.toEntity(entryHistoryDto);
        EntryHistory updated = entryHistoryService.updateEntryHistory(id, entryHistoryDetails);
        EntryHistoryDto updatedDto = mapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/entry-history/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntryHistory(@PathVariable Integer id) {
        entryHistoryService.deleteEntryHistory(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/entry-history/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EntryHistoryDto>> findByUserId(@PathVariable Integer userId) {
        List<EntryHistory> results = entryHistoryService.findByUserId(userId);
        List<EntryHistoryDto> resultDtos = results.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDtos);
    }

}
