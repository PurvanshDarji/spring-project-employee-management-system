package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Employee;
import com.example.demo.entity.LeaveRequest;
import com.example.demo.repository.LeaveRepository;           // ✅ Count
import com.example.demo.repository.LeaveRequestRepository;  // ✅ Applications
import com.example.demo.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/leave")
@CrossOrigin("*")
public class LeaveController {

    @Autowired private LeaveRepository leaveRepo;              // ✅ Dashboard counts
    @Autowired private LeaveRequestRepository leaveRequestRepo; // ✅ Leave applications
    @Autowired private EmployeeRepository employeeRepo;

    // ================= APPLY LEAVE ✅
    @PostMapping("/api/apply")
    @ResponseBody
    public ResponseEntity<?> applyLeave(@RequestBody LeaveRequest leave, HttpSession session) {
        try {
            Integer empId = (Integer) session.getAttribute("employeeId");
            if (empId == null) {
                return ResponseEntity.badRequest().body("Please login first");
            }

            Employee emp = employeeRepo.findById(empId).orElse(null);
            if (emp != null) {
                leave.setEmpId("EMP" + (1000 + empId));
                leave.setEmpName(emp.getName());
                leave.setDepartment(emp.getDepartment());
            }
            leave.setStatus("Pending");
            
            LeaveRequest saved = leaveRequestRepo.save(leave);  // ✅ LeaveRequestRepo
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ================= GET MY LEAVES ✅
    @GetMapping("/api/employee")
    @ResponseBody
    public ResponseEntity<?> getMyLeaves(HttpSession session) {
        try {
            Integer empId = (Integer) session.getAttribute("employeeId");
            if (empId == null) {
                return ResponseEntity.badRequest().body("Please login first");
            }

            String empIdStr = "EMP" + (1000 + empId);
            List<LeaveRequest> leaves = leaveRequestRepo.findByEmpIdOrderByIdDesc(empIdStr);  // ✅ LeaveRequestRepo
            return ResponseEntity.ok(leaves);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ================= GET LEAVES BY PHONE ✅
    @GetMapping("/api/employee/{phone}")
    @ResponseBody
    public ResponseEntity<?> getLeavesByPhone(@PathVariable String phone) {
        List<LeaveRequest> leaves = leaveRequestRepo.findByEmpId(phone);  // ✅ LeaveRequestRepo
        return ResponseEntity.ok(leaves);
    }

    // ================= PENDING LEAVES (Admin) ✅
    @GetMapping("/api/pending")
    @ResponseBody
    public ResponseEntity<?> getPendingLeaves() {
        List<LeaveRequest> pending = leaveRequestRepo.findByStatusOrderByIdDesc("Pending");  // ✅ LeaveRequestRepo
        return ResponseEntity.ok(pending);
    }

    // ================= APPROVE LEAVE ✅
    @PostMapping("/api/{id}/approve")
    @ResponseBody
    public ResponseEntity<String> approveLeave(@PathVariable Integer id) {
        try {
            LeaveRequest leave = leaveRequestRepo.findById(id).orElse(null);
            if (leave != null) {
                leave.setStatus("Approved");
                leaveRequestRepo.save(leave);
                return ResponseEntity.ok("✅ Approved!");
            }
            return ResponseEntity.badRequest().body("Leave not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ================= REJECT LEAVE ✅
    @PostMapping("/api/{id}/reject")
    @ResponseBody
    public ResponseEntity<String> rejectLeave(@PathVariable Integer id, @RequestParam String reason) {
        try {
            LeaveRequest leave = leaveRequestRepo.findById(id).orElse(null);
            if (leave != null) {
                leave.setStatus("Rejected - " + reason);
                leaveRequestRepo.save(leave);
                return ResponseEntity.ok("❌ Rejected!");
            }
            return ResponseEntity.badRequest().body("Leave not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ================= DASHBOARD COUNT ✅
    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<?> getLeaveStats(HttpSession session) {
        try {
            Integer empId = (Integer) session.getAttribute("employeeId");
            if (empId == null) return ResponseEntity.badRequest().body("Login required");

            String empIdStr = "EMP" + (1000 + empId);
            long totalLeaves = leaveRepo.countByEmpId(empIdStr);  // ✅ LeaveRepo (count)
            
            return ResponseEntity.ok(Map.of("totalLeaves", totalLeaves));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}