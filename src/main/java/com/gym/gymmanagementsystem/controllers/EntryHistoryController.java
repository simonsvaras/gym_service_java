package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.EntryHistoryDto;
import com.gym.gymmanagementsystem.dto.mappers.EntryHistoryMapper;
import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.EntryHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu historie vstupů v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/entry-history")
 */
@RestController
@RequestMapping("/api/entry-history")
public class EntryHistoryController {

    private final EntryHistoryService entryHistoryService;

    @Autowired
    private EntryHistoryMapper mapper;

    /**
     * Konstruktor pro injektování služby historie vstupů.
     *
     * @param entryHistoryService Služba pro správu historie vstupů.
     */
    public EntryHistoryController(EntryHistoryService entryHistoryService) {
        this.entryHistoryService = entryHistoryService;
    }

    /**
     * Získá seznam všech záznamů historie vstupů.
     *
     * @return ResponseEntity obsahující seznam DTO záznamů historie vstupů.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<EntryHistoryDto>> getAllEntryHistories() {
        List<EntryHistory> histories = entryHistoryService.getAllEntryHistories();
        List<EntryHistoryDto> historyDtos = histories.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(historyDtos);
    }

    /**
     * Získá záznam historie vstupu podle jeho ID.
     *
     * @param id ID záznamu historie vstupu.
     * @return ResponseEntity obsahující DTO záznamu historie vstupu.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntryHistoryDto> getEntryHistoryById(@PathVariable Integer id) {
        EntryHistory history = entryHistoryService.getEntryHistoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntryHistory nenalezena s ID " + id));
        EntryHistoryDto dto = mapper.toDto(history);
        return ResponseEntity.ok(dto);
    }

    /**
     * Získá seznam záznamů historie vstupů v zadaném časovém rozsahu.
     *
     * @param start Počáteční datum a čas rozsahu.
     * @param end    Konečné datum a čas rozsahu.
     * @return ResponseEntity obsahující seznam DTO záznamů historie vstupů v zadaném rozsahu.
     *
     * @getMapping("/range")
     */
    @GetMapping("/range")
    public ResponseEntity<List<EntryHistoryDto>> getEntriesInRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<EntryHistory> records = entryHistoryService.getEntriesInRange(start, end);
        List<EntryHistoryDto> dtos = records.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Vytvoří nový záznam historie vstupu.
     *
     * @param entryHistoryDto DTO obsahující informace o novém záznamu historie vstupu.
     * @return ResponseEntity obsahující vytvořenou DTO záznamu historie vstupu.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<EntryHistoryDto> createEntryHistory(@Valid @RequestBody EntryHistoryDto entryHistoryDto) {
        EntryHistory entryHistory = mapper.toEntity(entryHistoryDto);
        EntryHistory created = entryHistoryService.createEntryHistory(entryHistory);
        EntryHistoryDto createdDto = mapper.toDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    /**
     * Aktualizuje informace o záznamu historie vstupu podle jeho ID.
     *
     * @param id               ID záznamu historie vstupu, který chceme aktualizovat.
     * @param entryHistoryDto  DTO obsahující aktualizované informace o záznamu historie vstupu.
     * @return ResponseEntity obsahující aktualizované DTO záznamu historie vstupu.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntryHistoryDto> updateEntryHistory(@PathVariable Integer id,
                                                              @Valid @RequestBody EntryHistoryDto entryHistoryDto) {
        EntryHistory entryHistoryDetails = mapper.toEntity(entryHistoryDto);
        EntryHistory updated = entryHistoryService.updateEntryHistory(id, entryHistoryDetails);
        EntryHistoryDto updatedDto = mapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže záznam historie vstupu podle jeho ID.
     *
     * @param id ID záznamu historie vstupu, který chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntryHistory(@PathVariable Integer id) {
        entryHistoryService.deleteEntryHistory(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Získá seznam záznamů historie vstupů podle ID uživatele.
     *
     * @param userId ID uživatele.
     * @return ResponseEntity obsahující seznam DTO záznamů historie vstupů.
     *
     * @getMapping("/user/{userId}")
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EntryHistoryDto>> findByUserId(@PathVariable Integer userId) {
        List<EntryHistory> results = entryHistoryService.findByUserId(userId);
        List<EntryHistoryDto> resultDtos = results.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDtos);
    }

}
