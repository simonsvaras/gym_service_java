package com.gym.gymmanagementsystem.dto.mappers;


import com.gym.gymmanagementsystem.dto.EmployeeDto;
import com.gym.gymmanagementsystem.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDto toDto(Employee employee);
    Employee toEntity(EmployeeDto employeeDto);
}
