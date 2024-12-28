package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Integer id);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Integer id, Employee employeeDetails);
    void deleteEmployee(Integer id);
}
