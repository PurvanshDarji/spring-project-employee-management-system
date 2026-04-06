package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/department")
@CrossOrigin("*")
public class DepartmentController {

    @Autowired private DepartmentRepository deptRepo;
    @Autowired private EmployeeRepository empRepo;

    @GetMapping("/employees")
    public List<Employee> getEmployees(){
        return empRepo.findAll();
    }

    @PostMapping("/save")
    public Department save(@RequestBody Department dept){
        System.out.println("Saving: " + dept.getName()); // ✅ Debug
        return deptRepo.save(dept);
    }

    @PostMapping("/update")  // ✅ POST instead of PUT
    public Department update(@RequestBody Department dept){
        System.out.println("Updating ID: " + dept.getId() + ", Name: " + dept.getName()); // ✅ Debug
        return deptRepo.save(dept);
    }

    @GetMapping("/all")
    public List<Department> getAll(){
        return deptRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable int id) {
        Optional<Department> dept = deptRepo.findById(id);
        return dept.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        if(deptRepo.existsById(id)) {
            deptRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}