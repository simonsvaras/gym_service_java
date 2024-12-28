package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.EmployeeDto;
import com.gym.gymmanagementsystem.dto.mappers.EmployeeMapper;
import com.gym.gymmanagementsystem.entities.Employee;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper mapper = EmployeeMapper.INSTANCE;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET /api/employees
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDto> employeeDtos = employees.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeDtos);
    }

    // GET /api/employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        EmployeeDto dto = mapper.toDto(employee);
        return ResponseEntity.ok(dto);
    }

    // POST /api/employees
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee employee = mapper.toEntity(employeeDto);
        Employee createdEmployee = employeeService.createEmployee(employee);
        EmployeeDto createdDto = mapper.toDto(createdEmployee);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/employees/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Integer id,
                                                      @Valid @RequestBody EmployeeDto employeeDto) {
        Employee employeeDetails = mapper.toEntity(employeeDto);
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        EmployeeDto updatedDto = mapper.toDto(updatedEmployee);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/employees/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
