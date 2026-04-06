package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Salary;                  // ✅ Salary import
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.SalaryRepository;
import com.example.demo.service.SalaryService;
import java.util.List;

@Controller
@RequestMapping("/api")
public class SalaryController {

    @Autowired private SalaryService salaryService;
    @Autowired private EmployeeRepository employeeRepo;
    @Autowired private SalaryRepository salaryRepo;

    // ── Employee: Salary Status Page ──────────────────────────
    @GetMapping("/salary/status")
    public String salaryStatusPage(HttpSession session, Model model) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";

        Employee emp = employeeRepo.findById(empId).orElse(null);
        Salary salary = salaryRepo.findByEmpId(empId);  // ✅ direct repo

        model.addAttribute("employee", emp);
        model.addAttribute("salary", salary);
        return "salary-status";
    }

    // ── Employee: Salary JSON for JS ───────────────────────────
    @GetMapping("/salary/data")
    @ResponseBody
    public Salary getSalaryData(HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return new Salary();

        Salary salary = salaryRepo.findByEmpId(empId);
        if (salary == null) return new Salary();

        return salary;
    }

    // ── Admin: All Employees ───────────────────────────────────
    @GetMapping("/salary/employees")
    @ResponseBody
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    // ── Admin: All Salaries ────────────────────────────────────
    @GetMapping("/salary/all")
    @ResponseBody
    public List<Salary> getAllSalaries() {
        return salaryRepo.findAll();
    }

    // ── Admin: Save Salary ─────────────────────────────────────
    @PostMapping("/salary/save")
    @ResponseBody
    public String saveSalary(@RequestBody Salary salary) {
        try {
            double net = salary.getBaseSalary()
                       + salary.getAllowance()
                       - salary.getDeduction();
            salary.setNetSalary(net);
            salaryRepo.save(salary);
            return "Salary Saved Successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ── Admin: Delete Salary ───────────────────────────────────
    @DeleteMapping("/salary/{id}")
    @ResponseBody
    public String deleteSalary(@PathVariable int id) {
        try {
            salaryRepo.deleteById(id);
            return "Salary Deleted Successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}