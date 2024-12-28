package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.UserDto;
import com.gym.gymmanagementsystem.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
