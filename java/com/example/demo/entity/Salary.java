	package com.example.demo.entity;
	
	import jakarta.persistence.*;
	
	@Entity
	@Table(name="salary")
	public class Salary {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    
	    @Column(name = "emp_id")
	    private int empId;
	    
	    @Column(name = "emp_name")
	    private String empName;
	    
	    private String department;
	    
	    @Column(name = "base_salary")
	    private double baseSalary;
	    
	    private double allowance;
	    
	    private double deduction;
	    
	    @Column(name = "net_salary")
	    private double netSalary;
	
	    // Default Constructor (JPA के लिए जरूरी)
	    public Salary() {}
	
	    // Getters & Setters (सभी पहले से हैं)
	    public int getId() { return id; }
	    public void setId(int id) { this.id = id; }
	    
	    public int getEmpId() { return empId; }
	    public void setEmpId(int empId) { this.empId = empId; }
	    
	    public String getEmpName() { return empName; }
	    public void setEmpName(String empName) { this.empName = empName; }
	    
	    public String getDepartment() { return department; }
	    public void setDepartment(String department) { this.department = department; }
	    
	    public double getBaseSalary() { return baseSalary; }
	    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
	    
	    public double getAllowance() { return allowance; }
	    public void setAllowance(double allowance) { this.allowance = allowance; }
	    
	    public double getDeduction() { return deduction; }
	    public void setDeduction(double deduction) { this.deduction = deduction; }
	    
	    public double getNetSalary() { return netSalary; }
	    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
	}