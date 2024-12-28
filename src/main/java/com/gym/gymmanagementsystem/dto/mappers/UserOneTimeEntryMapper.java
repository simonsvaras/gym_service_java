package com.gym.gymmanagementsystem.dto.mappers;


import com.gym.gymmanagementsystem.dto.UserOneTimeEntryDto;
import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserOneTimeEntryMapper {
    UserOneTimeEntryMapper INSTANCE = Mappers.getMapper(UserOneTimeEntryMapper.class);

    UserOneTimeEntryDto toDto(UserOneTimeEntry userOneTimeEntry);
    UserOneTimeEntry toEntity(UserOneTimeEntryDto userOneTimeEntryDto);
}
