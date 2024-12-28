package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.Employee;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        // Případná validace nebo další logika
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Integer id, Employee employeeDetails) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setUsername(employeeDetails.getUsername());
                    employee.setFirstname(employeeDetails.getFirstname());
                    employee.setLastname(employeeDetails.getLastname());
                    employee.setPassword(employeeDetails.getPassword());
                    employee.setRole(employeeDetails.getRole());
                    // Aktualizujte další pole podle potřeby
                    return employeeRepository.save(employee);
                }).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    @Override
    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        employeeRepository.delete(employee);
    }
}
