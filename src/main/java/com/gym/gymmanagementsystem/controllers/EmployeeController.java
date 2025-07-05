package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.EmployeeDto;
import com.gym.gymmanagementsystem.dto.mappers.EmployeeMapper;
import com.gym.gymmanagementsystem.entities.Employee;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu zaměstnanců v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/employees")
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    private EmployeeMapper mapper;

    /**
     * Konstruktor pro injektování služby zaměstnanců.
     *
     * @param employeeService Služba pro správu zaměstnanců.
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Získá seznam všech zaměstnanců.
     *
     * @return ResponseEntity obsahující seznam DTO zaměstnanců.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDto> employeeDtos = employees.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeDtos);
    }

    /**
     * Získá zaměstnance podle jeho ID.
     *
     * @param id ID zaměstnance.
     * @return ResponseEntity obsahující DTO zaměstnance.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zaměstnanec nenalezen s ID " + id));
        EmployeeDto dto = mapper.toDto(employee);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří nového zaměstnance.
     *
     * @param employeeDto DTO obsahující informace o novém zaměstnanci.
     * @return ResponseEntity obsahující vytvořenou DTO zaměstnance.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        // Logování přijatého EmployeeDto místo System.out.println
        // Doporučuje se použít Logger místo System.out.println
        // logger.info("Přijatý EmployeeDto: {}", employeeDto);
        Employee employee = mapper.toEntity(employeeDto);
        Employee createdEmployee = employeeService.createEmployee(employee);
        EmployeeDto createdDto = mapper.toDto(createdEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    /**
     * Aktualizuje informace o zaměstnanci podle jeho ID.
     *
     * @param id          ID zaměstnance, kterého chceme aktualizovat.
     * @param employeeDto DTO obsahující aktualizované informace o zaměstnanci.
     * @return ResponseEntity obsahující aktualizované DTO zaměstnance.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Integer id,
                                                      @Valid @RequestBody EmployeeDto employeeDto) {
        Employee employeeDetails = mapper.toEntity(employeeDto);
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        EmployeeDto updatedDto = mapper.toDto(updatedEmployee);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže zaměstnance podle jeho ID.
     *
     * @param id ID zaměstnance, kterého chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
