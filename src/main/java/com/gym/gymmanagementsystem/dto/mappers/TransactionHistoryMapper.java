package com.gym.gymmanagementsystem.dto.mappers;


import com.gym.gymmanagementsystem.dto.TransactionHistoryDto;
import com.gym.gymmanagementsystem.entities.TransactionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionHistoryMapper {
    TransactionHistoryMapper INSTANCE = Mappers.getMapper(TransactionHistoryMapper.class);

    TransactionHistoryDto toDto(TransactionHistory transactionHistory);
    TransactionHistory toEntity(TransactionHistoryDto transactionHistoryDto);
}
