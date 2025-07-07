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
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(UserOneTimeEntryController.class);

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
        log.info("GET /api/user-one-time-entries");
        List<UserOneTimeEntry> entries = userOneTimeEntryService.getAllUserOneTimeEntries();
        List<UserOneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} záznamů", entryDtos.size());
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
        log.info("GET /api/user-one-time-entries/{}", id);
        UserOneTimeEntry entry = userOneTimeEntryService.getUserOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry nenalezen s ID " + id));
        UserOneTimeEntryDto dto = mapper.toDto(entry);
        log.debug("Jednorázový vstup uživatele {} nalezen", id);
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
        log.info("POST /api/user-one-time-entries count={} - {}", count, userOneTimeEntryDto);

        // Podmíněná validace pro oneTimeEntryID = 3
        if (userOneTimeEntryDto.getOneTimeEntryID() != null && userOneTimeEntryDto.getOneTimeEntryID().equals(3)) {
            if (userOneTimeEntryDto.getCustomPrice() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Pole customPrice je povinné pro oneTimeEntryID = 3"
                );
            }
        } else {
            if (userOneTimeEntryDto.getCustomPrice() != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "customPrice lze zadat pouze pro oneTimeEntryID = 3"
                );
            }
        }

        List<UserOneTimeEntryDto> createdDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            UserOneTimeEntry userOneTimeEntry = mapper.toEntity(userOneTimeEntryDto);
            UserOneTimeEntry created = userOneTimeEntryService.createUserOneTimeEntry(
                    userOneTimeEntry, userOneTimeEntryDto.getCustomPrice());
            UserOneTimeEntryDto createdDto = mapper.toDto(created);
            createdDtos.add(createdDto);
        }

        log.debug("Vytvořeno {} záznamů", createdDtos.size());
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
        log.info("PUT /api/user-one-time-entries/{} - {}", id, entryDto);
        UserOneTimeEntry entryDetails = mapper.toEntity(entryDto);
        UserOneTimeEntry updated = userOneTimeEntryService.updateUserOneTimeEntry(id, entryDetails);
        UserOneTimeEntryDto updatedDto = mapper.toDto(updated);
        log.debug("Jednorázový vstup uživatele {} aktualizován", id);
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
        log.info("DELETE /api/user-one-time-entries/{}", id);
        userOneTimeEntryService.deleteUserOneTimeEntry(id);
        log.debug("Jednorázový vstup uživatele {} smazán", id);
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
        log.info("GET /api/user-one-time-entries/user/{}", userId);
        List<UserOneTimeEntry> entries = userOneTimeEntryService.findByUserId(userId);
        List<UserOneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} záznamů", entryDtos.size());
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
        log.info("GET /api/user-one-time-entries/unused");
        List<UserOneTimeEntry> unused = userOneTimeEntryService.findUnusedEntries();
        List<UserOneTimeEntryDto> entryDtos = unused.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} nevyužitých záznamů", entryDtos.size());
        return ResponseEntity.ok(entryDtos);
    }
}
