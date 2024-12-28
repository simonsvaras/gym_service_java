package com.gym.gymmanagementsystem.dto.mappers;


import com.gym.gymmanagementsystem.dto.EntryHistoryDto;
import com.gym.gymmanagementsystem.entities.EntryHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntryHistoryMapper {
    EntryHistoryMapper INSTANCE = Mappers.getMapper(EntryHistoryMapper.class);

    EntryHistoryDto toDto(EntryHistory entryHistory);
    EntryHistory toEntity(EntryHistoryDto entryHistoryDto);
}
