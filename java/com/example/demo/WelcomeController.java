package com.example.demo;

// ================= IMPORTS =================
import java.util.*;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.LeaveRequest;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.LeaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class WelcomeController {

    // ================= DEPENDENCY INJECTION =================
    @Autowired private EmployeeService employeeService;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private LeaveService leaveService;
    @Autowired private LeaveRequestRepository leaveRequestRepository;
    @Autowired private FeedbackRepository feedbackRepository;

    // ==========================================================
    // PUBLIC PAGES (No Login Required)
    // ==========================================================
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    // ==========================================================
    // ADMIN DASHBOARD & PAGES
    // ==========================================================
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        long totalEmployees = employeeRepository.count();
        long departments = employeeRepository.countDepartments();
        List<Employee> recentEmployees = employeeRepository.findTop5ByOrderByIdDesc();
        Map<String, Long> leaveStats = leaveService.getLeaveStatistics();

        model.addAttribute("emp", totalEmployees);
        model.addAttribute("dept", departments);
        model.addAttribute("present", totalEmployees);
        model.addAttribute("leave", leaveStats.get("pending"));
        model.addAttribute("leaveStats", leaveStats);
        model.addAttribute("employees", recentEmployees);

        return "Admin Panel/Dashboard";
    }

   
    @GetMapping("/admin/add-employee")
    public String addEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "Admin Panel/Add_employee";
    }

    @PostMapping("/admin/employees/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "Admin Panel/Add_employee";
        }
        employeeService.saveEmployee(employee);
        return "redirect:/admin/add-employee?success";
    }

    @GetMapping("/admin/employees")
    public String employees(Model model) {
        model.addAttribute("employeeList", employeeService.getAllEmployees());
        return "Admin Panel/Employees";
    }

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/admin/departments")
    public String departments(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("departments", departmentRepository.findAll());

        if (id != null) {
            Department dept = departmentRepository.findById(id).orElse(null);
            model.addAttribute("dept", dept);
        } else {
            model.addAttribute("dept", new Department());
        }

        return "Admin Panel/D_management";
    }
    
    @PostMapping("/admin/save-department")
    public String saveDepartment(Department dept) {
        departmentRepository.save(dept); // id hoga to update, nahi hoga to insert
        return "redirect:/admin/departments";
    }
    
    @GetMapping("/admin/delete-department/{id}")
    public String deleteDepartment(@PathVariable int id) {
        departmentRepository.deleteById(id);
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/salary")
    public String salary() {
        return "Admin Panel/Salary";
    }

    @GetMapping("/admin/leave")
    public String adminLeave(Model model) {
        Map<String, Long> stats = leaveService.getLeaveStatistics();
        List<LeaveRequest> pendingLeaves = leaveService.getPendingLeaveRequests();
        model.addAttribute("stats", stats);
        model.addAttribute("pendingLeaves", pendingLeaves);
        return "Admin Panel/Leave";
    }

    // 🔥 FIXED ADMIN FEEDBACKS
    @GetMapping("/admin/feedbacks")
    public String adminFeedbacks(Model model, HttpSession session) {
        
        
        List<Feedback> allFeedbacks = feedbackRepository.findAllByOrderByIdDesc();
        long totalFeedbacks = feedbackRepository.count();
        
        model.addAttribute("feedbacks", allFeedbacks);
        model.addAttribute("totalFeedbacks", totalFeedbacks);
        return "Admin Panel/feedbacks";
    }
    
    @GetMapping("/admin/logs")
    public String logs() {
        return "Admin Panel/Logs";
    }

    // ==========================================================
    // ADMIN AJAX APIs
    // ==========================================================
    
    @GetMapping("/admin/api/leaves")
    @ResponseBody
    public List<LeaveRequest> getPendingLeaves() {
        return leaveService.getPendingLeaveRequests();
    }

    @PostMapping("/admin/api/leaves/{id}/approve")
    @ResponseBody
    public String approveLeave(@PathVariable int id) {
        try {
            leaveService.approveLeave(id);
            return "✅ Leave Approved Successfully!";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    @PostMapping("/admin/api/leaves/{id}/reject")
    @ResponseBody
    public String rejectLeave(@PathVariable int id, @RequestParam String reason) {
        try {
            leaveService.rejectLeave(id, reason);
            return "❌ Leave Rejected Successfully!";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    @GetMapping("/admin/api/stats")
    @ResponseBody
    public Map<String, Long> getLeaveStats() {
        return leaveService.getLeaveStatistics();
    }

    // ==========================================================
    // 🔥 NEW EMPLOYEE FEEDBACK ROUTES
    // ==========================================================

    @GetMapping("/employee/feedback-form")
    public String employeeFeedbackForm(HttpSession session, Model model) {

        Employee emp = (Employee) session.getAttribute("user");  // 🔥 FIX

        if (emp == null) return "redirect:/login";

        model.addAttribute("empId", "EMP" + emp.getId());
        model.addAttribute("empName", emp.getName());
        model.addAttribute("department", emp.getDepartment());

        return "User Panel/feedback-form";
    }

    @PostMapping("/employee/feedback/save")
    public String saveEmployeeFeedback(
            @RequestParam String empId,
            @RequestParam String empName,
            @RequestParam String department,
            @RequestParam String subject,
            @RequestParam String message,
            Model model) {
        try {
            Feedback feedback = new Feedback();
            feedback.setEmpId(empId);
            feedback.setEmpName(empName);
            feedback.setDepartment(department);
            feedback.setSubject(subject);
            feedback.setMessage(message);
            feedbackRepository.save(feedback);
            model.addAttribute("success", "✅ Feedback submitted!");
        } catch (Exception e) {
            model.addAttribute("error", "❌ " + e.getMessage());
        }
        return "redirect:/employee/feedback-form";
    }

    @GetMapping("/employee/feedback-list")
    public String employeeFeedbackList(HttpSession session, Model model) {
        Integer empIdInt = (Integer) session.getAttribute("employeeId");
        if (empIdInt == null) return "redirect:/login";

        Employee emp = employeeRepository.findById(empIdInt).orElse(null);
        if (emp == null) return "redirect:/login";

        List<Feedback> myFeedbacks = feedbackRepository
                .findByEmpIdOrderByIdDesc("EMP" + emp.getId());

        model.addAttribute("myFeedbacks", myFeedbacks);
        model.addAttribute("employee", emp);
        return "User Panel/feedback-list";
    }

  

    // ==========================================================
    // EMPLOYEE PAGES (Existing)
    // ==========================================================
    
    @GetMapping("/employee/leave")
    public String employeeLeave(HttpSession session, Model model) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if(empId == null) return "redirect:/login";

        Employee emp = employeeRepository.findById(empId).orElse(null);
        if(emp == null) return "redirect:/login";

        model.addAttribute("empId", emp.getPhone());
        model.addAttribute("empName", emp.getName());
        model.addAttribute("department", emp.getDepartment());
        session.setAttribute("employee", emp);

        return "User Panel/Leave_Request_Employee";
    }

    @PostMapping("/employee/leave/submit")
    public String submitLeave(@RequestParam String empId,
                              @RequestParam String empName,
                              @RequestParam String department,
                              @RequestParam String leaveType,
                              @RequestParam String fromDate,
                              @RequestParam String toDate,
                              @RequestParam String reason,
                              Model model) {
        try {
            LeaveRequest leave = new LeaveRequest();
            leave.setEmpId(empId);
            leave.setEmpName(empName);
            leave.setDepartment(department);
            leave.setLeaveType(leaveType);
            leave.setFromDate(fromDate);
            leave.setToDate(toDate);
            leave.setReason(reason);
            leave.setStatus("Pending");
            leaveRequestRepository.save(leave);
            model.addAttribute("success", "✅ Leave Request Submitted! Waiting for Approval.");
        } catch (Exception e) {
            model.addAttribute("error", "❌ Error: " + e.getMessage());
        }
        return "User Panel/Leave_Request_Employee";
    }

    @GetMapping("/employee/my-leaves")
    public String myLeaves(HttpSession session, Model model) {
        Integer empIdInt = (Integer) session.getAttribute("employeeId");
        if(empIdInt == null) return "redirect:/login";

        Employee emp = employeeRepository.findById(empIdInt).orElse(null);
        if(emp == null) return "redirect:/login";

        List<LeaveRequest> myLeaves = leaveRequestRepository
                .findByEmpIdOrderByIdDesc(emp.getPhone());

        model.addAttribute("myLeaves", myLeaves);
        model.addAttribute("employee", emp);
        return "User Panel/My_Leaves";
    }

    // ==========================================================
    // LOGOUT
    // ==========================================================
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}