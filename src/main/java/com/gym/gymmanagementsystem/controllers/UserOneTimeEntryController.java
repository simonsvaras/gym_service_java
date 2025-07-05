package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.UserOneTimeEntryDto;
import com.gym.gymmanagementsystem.dto.mappers.UserOneTimeEntryMapper;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.UserOneTimeEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu jednorázových vstupů uživatelů v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/user-one-time-entries")
 */
@RestController
@RequestMapping("/api/user-one-time-entries")
public class UserOneTimeEntryController {

    private final UserOneTimeEntryService userOneTimeEntryService;

    @Autowired
    private UserOneTimeEntryMapper mapper;

    /**
     * Konstruktor pro injektování služby jednorázových vstupů uživatelů.
     *
     * @param userOneTimeEntryService Služba pro správu jednorázových vstupů uživatelů.
     */
    public UserOneTimeEntryController(UserOneTimeEntryService userOneTimeEntryService) {
        this.userOneTimeEntryService = userOneTimeEntryService;
    }

    /**
     * Získá seznam všech jednorázových vstupů uživatelů.
     *
     * @return ResponseEntity obsahující seznam DTO jednorázových vstupů uživatelů.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<UserOneTimeEntryDto>> getAllUserOneTimeEntries() {
        List<UserOneTimeEntry> entries = userOneTimeEntryService.getAllUserOneTimeEntries();
        List<UserOneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entryDtos);
    }

    /**
     * Získá jednorázový vstup uživatele podle jeho ID.
     *
     * @param id ID jednorázového vstupu uživatele.
     * @return ResponseEntity obsahující DTO jednorázového vstupu uživatele.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserOneTimeEntryDto> getUserOneTimeEntryById(@PathVariable Integer id) {
        UserOneTimeEntry entry = userOneTimeEntryService.getUserOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry nenalezen s ID " + id));
        UserOneTimeEntryDto dto = mapper.toDto(entry);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří nové jednorázové vstupy uživatele.
     *
     * @param userOneTimeEntryDto DTO obsahující informace o novém jednorázovém vstupu uživatele.
     * @param count                Počet vstupů k vytvoření (volitelný, výchozí hodnota je 1).
     * @return ResponseEntity obsahující seznam vytvořených DTO jednorázových vstupů uživatele.
     */
    @PostMapping
    public ResponseEntity<List<UserOneTimeEntryDto>> createUserOneTimeEntries(
            @Valid @RequestBody UserOneTimeEntryDto userOneTimeEntryDto,
            @RequestParam(value = "count", defaultValue = "1") int count) {

        List<UserOneTimeEntryDto> createdDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            UserOneTimeEntry userOneTimeEntry = mapper.toEntity(userOneTimeEntryDto);
            UserOneTimeEntry created = userOneTimeEntryService.createUserOneTimeEntry(userOneTimeEntry);
            UserOneTimeEntryDto createdDto = mapper.toDto(created);
            createdDtos.add(createdDto);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDtos);
    }

    /**
     * Aktualizuje informace o jednorázovém vstupu uživatele podle jeho ID.
     *
     * @param id               ID jednorázového vstupu uživatele, který chceme aktualizovat.
     * @param entryDto         DTO obsahující aktualizované informace o jednorázovém vstupu uživatele.
     * @return ResponseEntity obsahující aktualizované DTO jednorázového vstupu uživatele.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserOneTimeEntryDto> updateUserOneTimeEntry(@PathVariable Integer id,
                                                                      @Valid @RequestBody UserOneTimeEntryDto entryDto) {
        UserOneTimeEntry entryDetails = mapper.toEntity(entryDto);
        UserOneTimeEntry updated = userOneTimeEntryService.updateUserOneTimeEntry(id, entryDetails);
        UserOneTimeEntryDto updatedDto = mapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže jednorázový vstup uživatele podle jeho ID.
     *
     * @param id ID jednorázového vstupu uživatele, který chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserOneTimeEntry(@PathVariable Integer id) {
        userOneTimeEntryService.deleteUserOneTimeEntry(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Získá seznam jednorázových vstupů uživatelů podle ID uživatele.
     *
     * @param userId ID uživatele.
     * @return ResponseEntity obsahující seznam DTO jednorázových vstupů uživatelů.
     *
     * @getMapping("/user/{userId}")
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserOneTimeEntryDto>> findByUserId(@PathVariable Integer userId) {
        List<UserOneTimeEntry> entries = userOneTimeEntryService.findByUserId(userId);
        List<UserOneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entryDtos);
    }

    /**
     * Získá seznam nevyužitých jednorázových vstupů uživatelů.
     *
     * @return ResponseEntity obsahující seznam DTO nevyužitých jednorázových vstupů uživatelů.
     *
     * @getMapping("/unused")
     */
    @GetMapping("/unused")
    public ResponseEntity<List<UserOneTimeEntryDto>> findUnusedEntries() {
        List<UserOneTimeEntry> unused = userOneTimeEntryService.findUnusedEntries();
        List<UserOneTimeEntryDto> entryDtos = unused.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entryDtos);
    }
}
