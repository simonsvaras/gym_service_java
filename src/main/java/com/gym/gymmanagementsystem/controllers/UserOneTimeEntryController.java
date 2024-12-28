package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.UserOneTimeEntryDto;
import com.gym.gymmanagementsystem.dto.mappers.UserOneTimeEntryMapper;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.UserOneTimeEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-one-time-entries")
public class UserOneTimeEntryController {

    private final UserOneTimeEntryService userOneTimeEntryService;
    private final UserOneTimeEntryMapper mapper = UserOneTimeEntryMapper.INSTANCE;

    public UserOneTimeEntryController(UserOneTimeEntryService userOneTimeEntryService) {
        this.userOneTimeEntryService = userOneTimeEntryService;
    }

    // GET /api/user-one-time-entries
    @GetMapping
    public ResponseEntity<List<UserOneTimeEntryDto>> getAllUserOneTimeEntries() {
        List<UserOneTimeEntry> entries = userOneTimeEntryService.getAllUserOneTimeEntries();
        List<UserOneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entryDtos);
    }

    // GET /api/user-one-time-entries/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserOneTimeEntryDto> getUserOneTimeEntryById(@PathVariable Integer id) {
        UserOneTimeEntry entry = userOneTimeEntryService.getUserOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserOneTimeEntry not found with id " + id));
        UserOneTimeEntryDto dto = mapper.toDto(entry);
        return ResponseEntity.ok(dto);
    }

    // POST /api/user-one-time-entries
    @PostMapping
    public ResponseEntity<UserOneTimeEntryDto> createUserOneTimeEntry(@Valid @RequestBody UserOneTimeEntryDto userOneTimeEntryDto) {
        UserOneTimeEntry userOneTimeEntry = mapper.toEntity(userOneTimeEntryDto);
        UserOneTimeEntry created = userOneTimeEntryService.createUserOneTimeEntry(userOneTimeEntry);
        UserOneTimeEntryDto createdDto = mapper.toDto(created);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/user-one-time-entries/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserOneTimeEntryDto> updateUserOneTimeEntry(@PathVariable Integer id,
                                                                      @Valid @RequestBody UserOneTimeEntryDto entryDto) {
        UserOneTimeEntry entryDetails = mapper.toEntity(entryDto);
        UserOneTimeEntry updated = userOneTimeEntryService.updateUserOneTimeEntry(id, entryDetails);
        UserOneTimeEntryDto updatedDto = mapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/user-one-time-entries/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserOneTimeEntry(@PathVariable Integer id) {
        userOneTimeEntryService.deleteUserOneTimeEntry(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/user-one-time-entries/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserOneTimeEntryDto>> findByUserId(@PathVariable Integer userId) {
        List<UserOneTimeEntry> entries = userOneTimeEntryService.findByUserId(userId);
        List<UserOneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entryDtos);
    }

    // Speciální příklad: GET /api/user-one-time-entries/unused
    @GetMapping("/unused")
    public ResponseEntity<List<UserOneTimeEntryDto>> findUnusedEntries() {
        List<UserOneTimeEntry> unused = userOneTimeEntryService.findUnusedEntries();
        List<UserOneTimeEntryDto> entryDtos = unused.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entryDtos);
    }
}
