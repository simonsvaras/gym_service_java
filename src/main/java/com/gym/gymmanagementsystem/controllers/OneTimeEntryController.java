package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.OneTimeEntryDto;
import com.gym.gymmanagementsystem.dto.mappers.OneTimeEntryMapper;
import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.OneTimeEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu jednorázových vstupů v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/one-time-entries")
 */
@RestController
@RequestMapping("/api/one-time-entries")
public class OneTimeEntryController {

    private static final Logger log = LoggerFactory.getLogger(OneTimeEntryController.class);

    private final OneTimeEntryService oneTimeEntryService;

    @Autowired
    private OneTimeEntryMapper mapper;

    /**
     * Konstruktor pro injektování služby jednorázových vstupů.
     *
     * @param oneTimeEntryService Služba pro správu jednorázových vstupů.
     */
    public OneTimeEntryController(OneTimeEntryService oneTimeEntryService) {
        this.oneTimeEntryService = oneTimeEntryService;
    }

    /**
     * Získá seznam všech jednorázových vstupů.
     *
     * @return ResponseEntity obsahující seznam DTO jednorázových vstupů.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<OneTimeEntryDto>> getAllOneTimeEntries() {
        log.info("GET /api/one-time-entries");
        List<OneTimeEntry> entries = oneTimeEntryService.getAllOneTimeEntries();
        List<OneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} jednorázových vstupů", entryDtos.size());
        return ResponseEntity.ok(entryDtos);
    }

    /**
     * Získá jednorázový vstup podle jeho ID.
     *
     * @param id ID jednorázového vstupu.
     * @return ResponseEntity obsahující DTO jednorázového vstupu.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<OneTimeEntryDto> getOneTimeEntryById(@PathVariable Integer id) {
        log.info("GET /api/one-time-entries/{}", id);
        OneTimeEntry entry = oneTimeEntryService.getOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry nenalezena s ID " + id));
        OneTimeEntryDto dto = mapper.toDto(entry);
        log.debug("Jednorázový vstup {} nalezen", id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří nový jednorázový vstup.
     *
     * @param oneTimeEntryDto DTO obsahující informace o novém jednorázovém vstupu.
     * @return ResponseEntity obsahující vytvořenou DTO jednorázového vstupu.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<OneTimeEntryDto> createOneTimeEntry(@Valid @RequestBody OneTimeEntryDto oneTimeEntryDto) {
        log.info("POST /api/one-time-entries - {}", oneTimeEntryDto);
        OneTimeEntry oneTimeEntry = mapper.toEntity(oneTimeEntryDto);
        OneTimeEntry createdEntry = oneTimeEntryService.createOneTimeEntry(oneTimeEntry);
        OneTimeEntryDto createdDto = mapper.toDto(createdEntry);
        log.debug("Jednorázový vstup vytvořen s ID {}", createdEntry.getOneTimeEntryID());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    /**
     * Aktualizuje informace o jednorázovém vstupu podle jeho ID.
     *
     * @param id               ID jednorázového vstupu, který chceme aktualizovat.
     * @param oneTimeEntryDto  DTO obsahující aktualizované informace o jednorázovém vstupu.
     * @return ResponseEntity obsahující aktualizované DTO jednorázového vstupu.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<OneTimeEntryDto> updateOneTimeEntry(@PathVariable Integer id,
                                                              @Valid @RequestBody OneTimeEntryDto oneTimeEntryDto) {
        log.info("PUT /api/one-time-entries/{} - {}", id, oneTimeEntryDto);
        OneTimeEntry oneTimeEntryDetails = mapper.toEntity(oneTimeEntryDto);
        OneTimeEntry updatedEntry = oneTimeEntryService.updateOneTimeEntry(id, oneTimeEntryDetails);
        OneTimeEntryDto updatedDto = mapper.toDto(updatedEntry);
        log.debug("Jednorázový vstup {} aktualizován", id);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže jednorázový vstup podle jeho ID.
     *
     * @param id ID jednorázového vstupu, který chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOneTimeEntry(@PathVariable Integer id) {
        log.info("DELETE /api/one-time-entries/{}", id);
        oneTimeEntryService.deleteOneTimeEntry(id);
        log.debug("Jednorázový vstup {} smazán", id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
