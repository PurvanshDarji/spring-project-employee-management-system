package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String empId;
    private String date;
    private String status;

    private LocalTime punchIn;
    private LocalTime punchOut;
    private double totalHours;

    // ✅ Break fields
    private LocalTime breakStart;
    private LocalTime breakEnd;
    private boolean onBreak;       // abhi break pe hai ya nahi

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalTime getPunchIn() { return punchIn; }
    public void setPunchIn(LocalTime punchIn) { this.punchIn = punchIn; }

    public LocalTime getPunchOut() { return punchOut; }
    public void setPunchOut(LocalTime punchOut) { this.punchOut = punchOut; }

    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = totalHours; }

    public LocalTime getBreakStart() { return breakStart; }
    public void setBreakStart(LocalTime breakStart) { this.breakStart = breakStart; }

    public LocalTime getBreakEnd() { return breakEnd; }
    public void setBreakEnd(LocalTime breakEnd) { this.breakEnd = breakEnd; }

    public boolean isOnBreak() { return onBreak; }
    public void setOnBreak(boolean onBreak) { this.onBreak = onBreak; }
}