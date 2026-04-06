package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "emp_id")
    private String empId;
    
    @Column(name = "emp_name")
    private String empName;
    
    // 🔥 ADD THESE 2 MISSING FIELDS:
    @Column(name = "department")
    private String department;
    
    @Column(name = "leave_type")
    private String leaveType;
    
    @Column(name = "from_date")
    private String fromDate;
    
    @Column(name = "to_date")
    private String toDate;
    
    @Column(name = "reason")
    private String reason;
    
    @Column(name = "status")
    private String status = "Pending";

    public LeaveRequest() {}

    // 🔥 ADD THESE GETTERS/SETTERS:
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    // YOUR EXISTING GETTERS/SETTERS (KEEP ALL)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }
    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}