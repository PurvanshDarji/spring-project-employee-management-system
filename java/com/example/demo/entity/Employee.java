package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 🔥 ADD THIS FIELD:
    @Column(name = "emp_id", length = 50, unique = true)
    private String empId;  // ✅ For Feedback reference

    @NotBlank()
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank()
    @Column(nullable = false, length = 100)
    private String department;

    @NotBlank()
    @Column(nullable = false, length = 100)
    private String designation;

    @NotBlank()
    @Pattern(regexp = "^[0-9]{10}$")
    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    @NotBlank()
    @Size(min = 4)
    @Column(nullable = false)
    private String password;

    // 🔥 ADD THESE GETTERS/SETTERS:
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    // Existing getters/setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}