package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.UserSubscriptionDto;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserSubscriptionMapper {
    UserSubscriptionMapper INSTANCE = Mappers.getMapper(UserSubscriptionMapper.class);

    UserSubscriptionDto toDto(UserSubscription userSubscription);
    UserSubscription toEntity(UserSubscriptionDto userSubscriptionDto);
}
