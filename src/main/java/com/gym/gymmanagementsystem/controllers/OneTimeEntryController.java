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
        List<OneTimeEntry> entries = oneTimeEntryService.getAllOneTimeEntries();
        List<OneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
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
        OneTimeEntry entry = oneTimeEntryService.getOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry nenalezena s ID " + id));
        OneTimeEntryDto dto = mapper.toDto(entry);
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
        OneTimeEntry oneTimeEntry = mapper.toEntity(oneTimeEntryDto);
        OneTimeEntry createdEntry = oneTimeEntryService.createOneTimeEntry(oneTimeEntry);
        OneTimeEntryDto createdDto = mapper.toDto(createdEntry);
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
        OneTimeEntry oneTimeEntryDetails = mapper.toEntity(oneTimeEntryDto);
        OneTimeEntry updatedEntry = oneTimeEntryService.updateOneTimeEntry(id, oneTimeEntryDetails);
        OneTimeEntryDto updatedDto = mapper.toDto(updatedEntry);
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
        oneTimeEntryService.deleteOneTimeEntry(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
