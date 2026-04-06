package com.example.demo.service;

import java.util.List;
import com.example.demo.model.EmployeeDto;
import jakarta.validation.Valid;
import com.example.demo.entity.Employee;

public interface EmployeeService {

    // CREATE
    void saveEmployee(@Valid Employee employee);
    void saveEmployee(EmployeeDto dto);  // 🔥 Keep your DTO method

    // READ ALL
    List<Employee> getAllEmployees();

    // READ BY ID
    Employee getEmployeeById(Integer id);

    // 🔥 NEW FOR LEAVE SYSTEM
    Employee getEmployeeByPhone(String phone);

    // UPDATE
    void updateEmployee(Integer id, EmployeeDto dto);

    // DELETE
    void deleteEmployee(Integer id);
}