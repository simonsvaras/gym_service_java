package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.UserOneTimeEntryDto;
import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import com.gym.gymmanagementsystem.repositories.OneTimeEntryRepository;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserOneTimeEntryMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OneTimeEntryRepository oneTimeEntryRepository;

    // Převod z UserOneTimeEntryDto na UserOneTimeEntry entitu
    public UserOneTimeEntry toEntity(UserOneTimeEntryDto dto) {
        if (dto == null) {
            return null;
        }

        UserOneTimeEntry entry = new UserOneTimeEntry();
        entry.setUserOneTimeEntryID(dto.getUserOneTimeEntryID());

        if (dto.getUserID() != null) {
            User user = userRepository.findById(dto.getUserID()).orElse(null);
            entry.setUser(user);
        }

        if (dto.getOneTimeEntryID() != null) {
            OneTimeEntry oneTimeEntry = oneTimeEntryRepository.findById(dto.getOneTimeEntryID()).orElse(null);
            entry.setOneTimeEntry(oneTimeEntry);
        }

        entry.setPurchaseDate(dto.getPurchaseDate());
        entry.setIsUsed(dto.getIsUsed());

        return entry;
    }

    // Převod z UserOneTimeEntry entity na UserOneTimeEntryDto
    public UserOneTimeEntryDto toDto(UserOneTimeEntry entry) {
        if (entry == null) {
            return null;
        }

        UserOneTimeEntryDto dto = new UserOneTimeEntryDto();
        dto.setUserOneTimeEntryID(entry.getUserOneTimeEntryID());

        if (entry.getUser() != null) {
            dto.setUserID(entry.getUser().getUserID());
        }

        if (entry.getOneTimeEntry() != null) {
            dto.setOneTimeEntryID(entry.getOneTimeEntry().getOneTimeEntryID());
        }

        dto.setPurchaseDate(entry.getPurchaseDate());
        dto.setIsUsed(entry.getIsUsed());

        return dto;
    }
}
