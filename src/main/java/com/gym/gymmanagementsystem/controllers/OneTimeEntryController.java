package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.OneTimeEntryDto;
import com.gym.gymmanagementsystem.dto.mappers.OneTimeEntryMapper;
import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.OneTimeEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/one-time-entries")
public class OneTimeEntryController {

    private final OneTimeEntryService oneTimeEntryService;
    private final OneTimeEntryMapper mapper = OneTimeEntryMapper.INSTANCE;

    public OneTimeEntryController(OneTimeEntryService oneTimeEntryService) {
        this.oneTimeEntryService = oneTimeEntryService;
    }

    // GET /api/one-time-entries
    @GetMapping
    public ResponseEntity<List<OneTimeEntryDto>> getAllOneTimeEntries() {
        List<OneTimeEntry> entries = oneTimeEntryService.getAllOneTimeEntries();
        List<OneTimeEntryDto> entryDtos = entries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(entryDtos);
    }

    // GET /api/one-time-entries/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OneTimeEntryDto> getOneTimeEntryById(@PathVariable Integer id) {
        OneTimeEntry entry = oneTimeEntryService.getOneTimeEntryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry not found with id " + id));
        OneTimeEntryDto dto = mapper.toDto(entry);
        return ResponseEntity.ok(dto);
    }

    // POST /api/one-time-entries
    @PostMapping
    public ResponseEntity<OneTimeEntryDto> createOneTimeEntry(@Valid @RequestBody OneTimeEntryDto oneTimeEntryDto) {
        OneTimeEntry oneTimeEntry = mapper.toEntity(oneTimeEntryDto);
        OneTimeEntry createdEntry = oneTimeEntryService.createOneTimeEntry(oneTimeEntry);
        OneTimeEntryDto createdDto = mapper.toDto(createdEntry);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/one-time-entries/{id}
    @PutMapping("/{id}")
    public ResponseEntity<OneTimeEntryDto> updateOneTimeEntry(@PathVariable Integer id,
                                                              @Valid @RequestBody OneTimeEntryDto oneTimeEntryDto) {
        OneTimeEntry oneTimeEntryDetails = mapper.toEntity(oneTimeEntryDto);
        OneTimeEntry updatedEntry = oneTimeEntryService.updateOneTimeEntry(id, oneTimeEntryDetails);
        OneTimeEntryDto updatedDto = mapper.toDto(updatedEntry);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/one-time-entries/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOneTimeEntry(@PathVariable Integer id) {
        oneTimeEntryService.deleteOneTimeEntry(id);
        return ResponseEntity.noContent().build();
    }
}
