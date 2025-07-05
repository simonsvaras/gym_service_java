package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.EntryHistoryDto;
import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntryHistoryMapper {

    @Autowired
    private UserRepository userRepository;

    // Převod z EntryHistoryDto na EntryHistory entitu
    public EntryHistory toEntity(EntryHistoryDto dto) {
        if (dto == null) {
            return null;
        }

        EntryHistory entryHistory = new EntryHistory();
        entryHistory.setEntryID(dto.getEntryID());

        if (dto.getUserID() != null) {
            User user = userRepository.findById(dto.getUserID()).orElse(null);
            entryHistory.setUser(user);
        }

        // entryDate je nastaveno automaticky v entitě, není zde mapováno
        entryHistory.setEntryType(dto.getEntryType());

        return entryHistory;
    }

    // Převod z EntryHistory entity na EntryHistoryDto
    public EntryHistoryDto toDto(EntryHistory entryHistory) {
        if (entryHistory == null) {
            return null;
        }

        EntryHistoryDto dto = new EntryHistoryDto();
        dto.setEntryID(entryHistory.getEntryID());

        if (entryHistory.getUser() != null) {
            dto.setUserID(entryHistory.getUser().getUserID());
            dto.setFirstName(entryHistory.getUser().getFirstname());
            dto.setLastName(entryHistory.getUser().getLastname());
        }

        dto.setEntryDate(entryHistory.getEntryDate());
        dto.setEntryType(entryHistory.getEntryType());

        return dto;
    }
}
