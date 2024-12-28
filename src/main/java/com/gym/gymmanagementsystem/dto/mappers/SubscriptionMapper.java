package com.gym.gymmanagementsystem.dto.mappers;


import com.gym.gymmanagementsystem.dto.SubscriptionDto;
import com.gym.gymmanagementsystem.entities.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscriptionMapper {
    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    SubscriptionDto toDto(Subscription subscription);
    Subscription toEntity(SubscriptionDto subscriptionDto);
}
