package com.gym.gymmanagementsystem.dto.mappers;


import com.gym.gymmanagementsystem.dto.CardDto;
import com.gym.gymmanagementsystem.entities.Card;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    CardDto toDto(Card card);
    Card toEntity(CardDto cardDto);
}
