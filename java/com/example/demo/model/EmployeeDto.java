package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;

public class EmployeeDto {

    @NotBlank()
    private String fullName;

    @NotBlank()
    private String department;

    @NotBlank()
    private String designation;

    @NotBlank()
    private String password;

    @NotBlank()
    private String phone;

    // ===== Getters and Setters =====

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}