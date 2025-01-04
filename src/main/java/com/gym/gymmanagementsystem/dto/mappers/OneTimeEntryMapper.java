package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.OneTimeEntryDto;
import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import org.springframework.stereotype.Component;

@Component
public class OneTimeEntryMapper {

    // Převod z OneTimeEntryDto na OneTimeEntry entitu
    public OneTimeEntry toEntity(OneTimeEntryDto dto) {
        if (dto == null) {
            return null;
        }

        OneTimeEntry entry = new OneTimeEntry();
        entry.setOneTimeEntryID(dto.getOneTimeEntryID());
        entry.setEntryName(dto.getEntryName());
        entry.setPrice(dto.getPrice());
        // userOneTimeEntries nejsou mapovány zde
        return entry;
    }

    // Převod z OneTimeEntry entity na OneTimeEntryDto
    public OneTimeEntryDto toDto(OneTimeEntry entry) {
        if (entry == null) {
            return null;
        }

        OneTimeEntryDto dto = new OneTimeEntryDto();
        dto.setOneTimeEntryID(entry.getOneTimeEntryID());
        dto.setEntryName(entry.getEntryName());
        dto.setPrice(entry.getPrice());
        return dto;
    }
}
