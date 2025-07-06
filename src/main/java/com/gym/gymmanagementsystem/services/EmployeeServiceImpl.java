package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.Employee;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Načítám všechny zaměstnance");
        List<Employee> list = employeeRepository.findAll();
        log.debug("Nalezeno {} zaměstnanců", list.size());
        return list;
    }

    @Override
    public Optional<Employee> getEmployeeById(Integer id) {
        log.info("Hledám zaměstnance id={}", id);
        return employeeRepository.findById(id);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        log.info("Vytvářím zaměstnance: {}", employee);
        Employee saved = employeeRepository.save(employee);
        log.debug("Zaměstnanec uložen s ID {}", saved.getIdEmployee());
        return saved;
    }

    @Override
    public Employee updateEmployee(Integer id, Employee employeeDetails) {
        log.info("Aktualizuji zaměstnance id={}", id);
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setUsername(employeeDetails.getUsername());
                    employee.setFirstname(employeeDetails.getFirstname());
                    employee.setLastname(employeeDetails.getLastname());
                    employee.setPassword(employeeDetails.getPassword());
                    employee.setRole(employeeDetails.getRole());
                    // Aktualizujte další pole podle potřeby
                    Employee e = employeeRepository.save(employee);
                    log.debug("Zaměstnanec {} aktualizován", id);
                    return e;
                }).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    @Override
    public void deleteEmployee(Integer id) {
        log.info("Mažu zaměstnance id={}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        employeeRepository.delete(employee);
        log.debug("Zaměstnanec {} smazán", id);
    }
}
