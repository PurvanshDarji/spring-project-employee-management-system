package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeDto;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    // ================= CONSTRUCTOR =================
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    // ================= SAVE USING DTO =================
    public void saveEmployee(EmployeeDto dto) {
        Employee emp = new Employee();
        emp.setName(dto.getFullName());
        emp.setDepartment(dto.getDepartment());
        emp.setDesignation(dto.getDesignation());
        emp.setPassword(dto.getPassword());
        emp.setPhone(dto.getPhone());
        repository.save(emp);
    }

    // ================= SAVE DIRECT EMPLOYEE =================
    @Override
    public void saveEmployee(@Valid Employee employee) {
        repository.save(employee);
    }

    // ================= READ ALL =================
    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // ================= READ BY ID =================
    @Override
    public Employee getEmployeeById(Integer id) {
        Optional<Employee> employee = repository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        }
        throw new RuntimeException("Employee Not Found with ID: " + id);
    }

    // ================= UPDATE =================
    @Override
    public void updateEmployee(Integer id, EmployeeDto dto) {
        Employee employee = getEmployeeById(id);
        employee.setName(dto.getFullName());
        employee.setDepartment(dto.getDepartment());
        employee.setDesignation(dto.getDesignation());
        employee.setPhone(dto.getPhone());
        employee.setPassword(dto.getPassword());
        repository.save(employee);
    }

    // ================= DELETE =================
    @Override
    public void deleteEmployee(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Employee Not Found with ID: " + id);
        }
        repository.deleteById(id);
    }

    // 🔥 NEW METHOD FOR LEAVE SYSTEM
    @Override
    public Employee getEmployeeByPhone(String phone) {
        Employee emp = repository.findByPhone(phone);
        if(emp == null) {
            throw new RuntimeException("Employee not found with phone: " + phone);
        }
        return emp;
    }
}