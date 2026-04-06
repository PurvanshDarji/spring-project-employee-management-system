package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    // ✅ Integer use karo (empId int hai Employee mein)
    Salary findByEmpId(Integer empId);
    
    Salary findByEmpId(int empId);

}