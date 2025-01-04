package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.EmployeeDto;
import com.gym.gymmanagementsystem.entities.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    // Převod z EmployeeDto na Employee entitu
    public Employee toEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setIdEmployee(dto.getIdEmployee());
        employee.setUsername(dto.getUsername());
        employee.setFirstname(dto.getFirstname());
        employee.setLastname(dto.getLastname());
        employee.setPassword(dto.getPassword());
        employee.setRole(dto.getRole());
        // createdAt je nastaveno automaticky v entitě
        return employee;
    }

    // Převod z Employee entity na EmployeeDto (bez pole password)
    public EmployeeDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDto dto = new EmployeeDto();
        dto.setIdEmployee(employee.getIdEmployee());
        dto.setUsername(employee.getUsername());
        dto.setFirstname(employee.getFirstname());
        dto.setLastname(employee.getLastname());
        // Heslo není zahrnuto v DTO pro odpovědi
        dto.setRole(employee.getRole());
        return dto;
    }
}
