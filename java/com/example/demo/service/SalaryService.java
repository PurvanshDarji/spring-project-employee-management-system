package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Salary;
import com.example.demo.repository.SalaryRepository;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    // Get salary by employee id
    public Salary getSalaryByEmpId(int empId) {

        Salary salary = salaryRepository.findByEmpId(empId);

        if (salary == null) {
            Salary empty = new Salary();
            empty.setEmpName("Not Found");
            empty.setDepartment("-");
            empty.setBaseSalary(0);
            empty.setAllowance(0);
            empty.setDeduction(0);
            empty.setNetSalary(0);
            return empty;
        }

        return salary;
    }
}