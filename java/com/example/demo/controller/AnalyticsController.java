package com.example.demo.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Attendance;
import com.example.demo.entity.Employee;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AnalyticsController {

    @Autowired private EmployeeRepository empRepo;
    @Autowired private AttendanceRepository attRepo;

    @GetMapping("/analytics")
    public String analytics(HttpSession session, Model model) {

        if (session.getAttribute("admin") == null) return "redirect:/login";

        List<Employee>   allEmp = empRepo.findAll();
        List<Attendance> allAtt = attRepo.findAll();
        String today = java.time.LocalDate.now().toString();

        // ✅ Total employees
        model.addAttribute("totalEmployees", allEmp.size());

        // ✅ Department wise count
        Map<String, Long> deptMap = new LinkedHashMap<>();
        for (Employee e : allEmp) {
            deptMap.merge(e.getDepartment(), 1L, Long::sum);
        }
        model.addAttribute("deptLabels", new ArrayList<>(deptMap.keySet()));
        model.addAttribute("deptCounts", new ArrayList<>(deptMap.values()));
        model.addAttribute("deptData",   deptMap);

        // ✅ Overall attendance counts
        long present = allAtt.stream().filter(a -> "Present".equals(a.getStatus())).count();
        long absent  = allAtt.stream().filter(a -> "Absent".equals(a.getStatus())).count();
        long halfDay = allAtt.stream().filter(a -> "Half Day".equals(a.getStatus())).count();
        model.addAttribute("presentCount", present);
        model.addAttribute("absentCount",  absent);
        model.addAttribute("halfDayCount", halfDay);

        // ✅ Today's attendance
        long todayPresent = allAtt.stream()
            .filter(a -> today.equals(a.getDate()) && "Present".equals(a.getStatus()))
            .count();
        long todayAbsent = Math.max(allEmp.size() - todayPresent, 0);
        model.addAttribute("todayPresent", todayPresent);
        model.addAttribute("todayAbsent",  todayAbsent);

        // ✅ Department wise attendance (present count per dept)
        Map<String, Long> deptPresent = new LinkedHashMap<>();
        for (String dept : deptMap.keySet()) {
            long count = allAtt.stream()
                .filter(a -> "Present".equals(a.getStatus()))
                .filter(a -> {
                    Employee e = empRepo.findByEmpId(a.getEmpId());
                    return e != null && dept.equals(e.getDepartment());
                })
                .count();
            deptPresent.put(dept, count);
        }
        model.addAttribute("deptPresentCounts", new ArrayList<>(deptPresent.values()));

        return "Admin Panel/Analytics";
    }
}