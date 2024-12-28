package com.gym.gymmanagementsystem.dto.mappers;


import com.gym.gymmanagementsystem.dto.OneTimeEntryDto;
import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OneTimeEntryMapper {
    OneTimeEntryMapper INSTANCE = Mappers.getMapper(OneTimeEntryMapper.class);

    OneTimeEntryDto toDto(OneTimeEntry oneTimeEntry);
    OneTimeEntry toEntity(OneTimeEntryDto oneTimeEntryDto);
}
