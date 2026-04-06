package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Employee;
import com.example.demo.entity.LeaveRequest;
import com.example.demo.entity.Salary;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.LeaveRepository;
import com.example.demo.repository.SalaryRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@CrossOrigin("*")
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeRepository repo;
    private final AttendanceRepository attendanceRepo;
    private final LeaveRepository leaveRepo;
    private final SalaryRepository salaryRepo;

    public EmployeeController(EmployeeRepository repo,
                              AttendanceRepository attendanceRepo,
                              LeaveRepository leaveRepo,
                              SalaryRepository salaryRepo) {
        this.repo = repo;
        this.attendanceRepo = attendanceRepo;
        this.leaveRepo = leaveRepo;
        this.salaryRepo = salaryRepo;
    }

    // ================= API ENDPOINTS (Admin Use) =================
    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<Employee> saveApi(@RequestBody Employee employee) {
        Employee savedEmployee = repo.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<Employee> employee = repo.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.badRequest().body("Employee Not Found");
    }

    @PutMapping("/api/update/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Employee updatedEmployee) {
        Optional<Employee> existingEmployee = repo.findById(id);
        if (existingEmployee.isEmpty()) {
            return ResponseEntity.badRequest().body("Employee Not Found");
        }

        Employee employee = existingEmployee.get();
        employee.setName(updatedEmployee.getName());
        employee.setDepartment(updatedEmployee.getDepartment());
        employee.setDesignation(updatedEmployee.getDesignation());
        employee.setPhone(updatedEmployee.getPhone());
        employee.setPassword(updatedEmployee.getPassword());
        repo.save(employee);
        return ResponseEntity.ok("Employee Updated Successfully");
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body("Employee Not Found");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Employee Deleted Successfully");
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";

        Optional<Employee> employeeOpt = repo.findById(empId);
        if (employeeOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        Employee employee = employeeOpt.get();
        String empName = employee.getName();
        String empIdStr = "EMP" + (1000 + employee.getId());

        long present = attendanceRepo.countByEmpIdAndStatus(empIdStr, "Present");
        long absent = attendanceRepo.countByEmpIdAndStatus(empIdStr, "Absent");
        long leave = leaveRepo.countByEmpId(empIdStr);

        Salary salary = salaryRepo.findByEmpId(empId);
        double netSalary = (salary != null) ? salary.getNetSalary() : 0;

        model.addAttribute("empName", empName);
        model.addAttribute("empId", empIdStr);
        model.addAttribute("present", present);
        model.addAttribute("absent", absent);
        model.addAttribute("leave", leave);
        model.addAttribute("salary", netSalary);
        model.addAttribute("employee", employee);

        return "User Panel/Employee_dashboard";
    }

    // ================= PROFILE =================
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";

        Optional<Employee> employeeOpt = repo.findById(empId);
        if (employeeOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        Employee employee = employeeOpt.get();
        model.addAttribute("employee", employee);
        return "User Panel/Employee_profile";
    }

    // ================= SALARY =================
    @GetMapping("/salary")
    public String salary(Model model, HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";

        Optional<Employee> employeeOpt = repo.findById(empId);
        if (employeeOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        Employee employee = employeeOpt.get();
        Salary salary = salaryRepo.findByEmpId(empId);
        
        if (salary == null) {
            salary = new Salary();
            salary.setEmpId(empId);
            salary.setEmpName(employee.getName());
            salary.setDepartment(employee.getDepartment());
            salary.setNetSalary(0);
        }

        model.addAttribute("salary", salary);
        model.addAttribute("employee", employee);
        model.addAttribute("empId", "EMP" + (1000 + empId));
        return "User Panel/Employee_salary";
    }

    // ================= UPDATE PROFILE =================
    @PostMapping("/update-profile")
    @ResponseBody
    public String updateProfile(@RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam(required = false) String password,
                                HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "fail";

        Optional<Employee> optionalEmployee = repo.findById(empId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setName(name);
            employee.setPhone(phone);

            if (password != null && !password.trim().isEmpty()) {
                employee.setPassword(password);
            }

            repo.save(employee);
            return "success";
        }
        return "fail";
    }
    
 // ================= ATTENDANCE =================
    @GetMapping("/attendance")
    public String attendance(Model model, HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";

        Optional<Employee> employeeOpt = repo.findById(empId);
        if (employeeOpt.isEmpty()) return "redirect:/login";
        Employee employee = employeeOpt.get();

        String empIdStr = "EMP" + (1000 + empId); // ✅ EMP1001 type

        model.addAttribute("empId",   empIdStr);          // ✅ String bhejo
        model.addAttribute("empName", employee.getName()); // ✅ Naam
        return "User Panel/Employee_attendance";
    }

    // ================= LEAVES =================
    @GetMapping("/leaves")
    public String leaves(Model model, HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";

        String empIdStr = "EMP" + (1000 + empId);
        model.addAttribute("empId", empId);
        model.addAttribute("empIdStr", empIdStr);
        return "User Panel/Employee_leaves";
    }

    // ================= INSIGHTS =================
 // ================= INSIGHTS =================
    @GetMapping("/insights")
    public String insights(HttpSession session, Model model) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";

        // ✅ empId string banao — same format jo attendance mein use hota hai
        String empIdStr = "EMP" + (1000 + empId);

        // ✅ Employee details
        Optional<Employee> employeeOpt = repo.findById(empId);
        if (employeeOpt.isEmpty()) return "redirect:/login";
        Employee employee = employeeOpt.get();

        // ✅ Attendance records
        List<com.example.demo.entity.Attendance> records = attendanceRepo.findByEmpId(empIdStr);

        long total   = records.size();
        long present = records.stream().filter(a -> "Present".equals(a.getStatus())).count();
        long absent  = records.stream().filter(a -> "Absent".equals(a.getStatus())).count();
        long halfDay = records.stream().filter(a -> "Half Day".equals(a.getStatus())).count();
        long leave   = leaveRepo.countByEmpId(empIdStr);

        // ✅ Percentages
        double attPercent  = total > 0 ? Math.round((present * 100.0) / total) : 0;
        double perfPercent = total > 0 ? Math.round(((present + halfDay * 0.5) * 100.0) / total) : 0;
        long punchedOut    = records.stream().filter(a -> a.getPunchOut() != null).count();
        double taskPercent = total > 0 ? Math.round((punchedOut * 100.0) / total) : 0;

        // ✅ Achievements
        java.util.List<String> achievements = new java.util.ArrayList<>();
        if (attPercent == 100) achievements.add("🏆 100% Attendance Achieved!");
        if (attPercent >= 90)  achievements.add("⭐ Excellent Attendance - " + (int)attPercent + "%");
        if (attPercent >= 75)  achievements.add("✅ Good Attendance Record");
        if (leave == 0)        achievements.add("🎯 No Leaves Taken!");
        if (present >= 20)     achievements.add("🚀 20+ Days Present This Period");
        if (halfDay == 0 && total > 0) achievements.add("💪 No Half Days - Full Commitment!");
        if (achievements.isEmpty()) achievements.add("📈 Keep going! Achievements unlock with attendance.");

        // ✅ Model
        model.addAttribute("empName",      employee.getName());
        model.addAttribute("empId",        empIdStr);
        model.addAttribute("attPercent",   (int) attPercent);
        model.addAttribute("perfPercent",  (int) perfPercent);
        model.addAttribute("taskPercent",  (int) taskPercent);
        model.addAttribute("leaveUsed",    leave);
        model.addAttribute("present",      present);
        model.addAttribute("absent",       absent);
        model.addAttribute("halfDay",      halfDay);
        model.addAttribute("totalDays",    total);
        model.addAttribute("achievements", achievements);

        return "User Panel/employee_insights";
    }

    // ================= FEEDBACK =================
    @GetMapping("/feedback")
    public String feedback(HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "redirect:/login";
        return "User Panel/Employy_Feedback"; // Fixed typo: Employy -> Employee
    }

    // ================= LOGOUT =================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ================= MARK ATTENDANCE =================
    @PostMapping("/mark-attendance")
    @ResponseBody
    public String markAttendance(@RequestParam String status, HttpSession session) {
        Integer empId = (Integer) session.getAttribute("employeeId");
        if (empId == null) return "fail";

        
       
        return "success";
    }

    // ================= APPLY LEAVE =================
 // ================= APPLY LEAVE =================
    @PostMapping("/apply-leave")
    @ResponseBody
    public String applyLeave(@RequestParam String fromDate,
                            @RequestParam String toDate,
                            @RequestParam String reason,
                            @RequestParam(required = false) String leaveType, // ✅ add karo
                            HttpSession session) {
        try {
            Integer empId = (Integer) session.getAttribute("employeeId");
            if (empId == null) return "fail";

            String empIdStr = "EMP" + (1000 + empId);

            // ✅ Session se Employee object lo
            Employee emp = (Employee) session.getAttribute("user");
            String empName = (emp != null) ? emp.getName()       : "Unknown";
            String empDept = (emp != null) ? emp.getDepartment() : "Unknown"; 

            LeaveRequest leave = new LeaveRequest();
            leave.setEmpId(empIdStr);
            leave.setEmpName(empName);
            leave.setDepartment(empDept);                                 
            leave.setLeaveType(leaveType != null ? leaveType : "General");
            leave.setFromDate(fromDate);
            leave.setToDate(toDate);
            leave.setReason(reason);
            leave.setStatus("Pending");

            leaveRepo.save(leave);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}