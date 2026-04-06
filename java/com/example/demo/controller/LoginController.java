package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private EmployeeRepository repo;

    // GET Login Page
    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session, 
                           @RequestParam(required = false) String error) {
        
        // Logout if already logged in
        if (session.getAttribute("user") != null || session.getAttribute("admin") != null) {
            session.invalidate();
        }
        
        if (error != null) {
            model.addAttribute("error", "Invalid phone number or password!");
        }
        
        return "login";  // login.html
    }

    // 🔥 POST Login - FIXED FOR AJAX + FORM
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        System.out.println("🔍 Login attempt - Phone: " + username);  // Debug

        // ADMIN LOGIN
        if ("admin".equals(username.trim()) && "admin123".equals(password)) {
            session.setAttribute("admin", true);
            return "redirect:/admin/dashboard";
        }

        // EMPLOYEE LOGIN
        Employee emp = repo.findByPhone(username.trim());
        
        System.out.println("🔍 Employee found: " + (emp != null ? emp.getName() : "NOT FOUND"));

        if (emp != null && emp.getPassword().equals(password.trim())) {
            session.setAttribute("user", emp);
            session.setAttribute("employeeId", emp.getId());
            System.out.println("✅ Login SUCCESS for: " + emp.getName());
            return "redirect:/employee/dashboard";
        }

        System.out.println("❌ Login FAILED");
        redirectAttributes.addFlashAttribute("error", "Invalid phone number or password!");
        return "redirect:/login";
    }
}