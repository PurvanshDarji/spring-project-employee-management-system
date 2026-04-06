package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Department;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.DepartmentRepository;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackRepository repo;
    
    @Autowired
    private DepartmentRepository deptRepo;

    // 🔥 FIXED: Current logged-in user data (empId as STRING)
    @GetMapping("/user")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        Map<String, Object> user = new HashMap<>();

        Employee emp = (Employee) session.getAttribute("user");
        if (emp != null) {
            user.put("empId", emp.getEmpId() != null ? emp.getEmpId() : emp.getId());  // 🔥 String empId first
            user.put("empName", emp.getName());
            user.put("department", emp.getDepartment());
            user.put("designation", emp.getDesignation());
            user.put("phone", emp.getPhone());
        }
        return user;
    }

    // 🔥 Get all departments from database
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        try {
            List<Department> departments = deptRepo.findAll();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    // 🔥 FIXED: Save feedback - 100% SAFE
    @PostMapping("/save")
    public ResponseEntity<?> saveFeedback(@RequestBody Map<String, Object> feedbackData, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(401)
                .body(Map.of("success", false, "message", "Login required!"));
        }

        try {
            String subject = String.valueOf(feedbackData.get("subject")).trim();
            String message = String.valueOf(feedbackData.get("message")).trim();
            String departmentId = feedbackData.get("departmentId") != null ? 
                String.valueOf(feedbackData.get("departmentId")).trim() : null;
            
            if (subject.isEmpty() || message.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Subject and message required!"));
            }
            
            Employee emp = (Employee) session.getAttribute("user");
            Feedback feedback = new Feedback();
            
            // ✅ PERFECT - Matches your entity exactly
            feedback.setEmpId(emp.getEmpId());
            feedback.setEmpName(emp.getName());
            feedback.setDepartment(departmentId != null ? departmentId : emp.getDepartment());
            feedback.setSubject(subject);
            feedback.setMessage(message);
            
            Feedback saved = repo.save(feedback);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "id", saved.getId(),
                "message", "Feedback submitted successfully!"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // 🔥 ADMIN SESSION SET
    @GetMapping("/set-admin")
    public ResponseEntity<Map<String, String>> setAdminSession(HttpSession session) {
        session.setAttribute("admin", true);
        return ResponseEntity.ok(Map.of("message", "Admin session set"));
    }

    // 🔥 Get all feedbacks (Admin only)
    @GetMapping("/all")
    public ResponseEntity<?> getAllFeedbacks(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Admin access required"));
        }

        try {
            List<Feedback> feedbacks = repo.findTop10ByOrderByIdDesc();
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }
}