package com.example.demo.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Attendance;
import com.example.demo.entity.Employee;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.repository.EmployeeRepository; // ✅ add karo
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/attendance")
@CrossOrigin("*")
public class AttendanceController {

    @Autowired
    private AttendanceRepository repo;

    @Autowired
    private EmployeeRepository empRepo; // ✅ add karo

    // ✅ Attendance Page
    @GetMapping("/page")
    public String attendancePage(HttpSession session, Model model) {
        Employee emp = (Employee) session.getAttribute("user");
        if (emp == null) return "redirect:/login";
        model.addAttribute("empId", emp.getEmpId());
        model.addAttribute("empName", emp.getName());
        return "user-panel/Employee_attendance";
    }

    // ✅ Punch In
    @ResponseBody
    @PostMapping("/in/{empId}")
    public String punchIn(@PathVariable String empId) {
        String today = LocalDate.now().toString();
        Attendance existing = repo.findByEmpIdAndDate(empId, today);
        if (existing != null) return "ALREADY_MARKED";

        Attendance att = new Attendance();
        att.setEmpId(empId);
        att.setDate(today);
        att.setPunchIn(LocalTime.now());
        att.setStatus("Present");
        att.setOnBreak(false);
        repo.save(att);
        return "PUNCHED_IN";
    }

    // ✅ Break Start
    @ResponseBody
    @PostMapping("/break/start/{empId}")
    public String breakStart(@PathVariable String empId) {
        String today = LocalDate.now().toString();
        Attendance att = repo.findByEmpIdAndDate(empId, today);

        if (att == null) return "NOT_PUNCHED_IN";
        if (att.getPunchOut() != null) return "ALREADY_PUNCHED_OUT";
        if (att.getBreakStart() != null) return "BREAK_ALREADY_TAKEN";
        if (att.isOnBreak()) return "ALREADY_ON_BREAK";

        att.setBreakStart(LocalTime.now());
        att.setOnBreak(true);
        repo.save(att);
        return "BREAK_STARTED";
    }

    // ✅ Break End
    @ResponseBody
    @PostMapping("/break/end/{empId}")
    public String breakEnd(@PathVariable String empId) {
        String today = LocalDate.now().toString();
        Attendance att = repo.findByEmpIdAndDate(empId, today);

        if (att == null) return "NOT_PUNCHED_IN";
        if (!att.isOnBreak()) return "NOT_ON_BREAK";

        att.setBreakEnd(LocalTime.now());
        att.setOnBreak(false);
        repo.save(att);
        return "BREAK_ENDED";
    }

    // ✅ Punch Out
    @ResponseBody
    @PostMapping("/out/{empId}")
    public String punchOut(@PathVariable String empId) {
        String today = LocalDate.now().toString();
        Attendance att = repo.findByEmpIdAndDate(empId, today);

        if (att == null) return "NOT_PUNCHED_IN";
        if (att.getPunchOut() != null) return "ALREADY_PUNCHED_OUT";
        if (att.isOnBreak()) return "ON_BREAK";

        LocalTime punchOutTime = LocalTime.now();
        att.setPunchOut(punchOutTime);

        Duration total = Duration.between(att.getPunchIn(), punchOutTime);
        double hours = total.toMinutes() / 60.0;

        if (att.getBreakStart() != null) {
            hours = hours - 0.5;
        }

        hours = Math.round(hours * 100.0) / 100.0;
        att.setTotalHours(hours);

        if (hours >= 5) {
            att.setStatus("Present");
        } else if (hours >= 4) {
            att.setStatus("Half Day");
        } else {
            att.setStatus("Absent");
        }

        repo.save(att);
        return "PUNCHED_OUT:" + hours;
    }

    // ✅ Get Attendance
    @ResponseBody
    @GetMapping("/employee/{empId}")
    public List<Attendance> getByEmp(@PathVariable String empId) {
        return repo.findByEmpId(empId);
    }

    // ✅ Today ka status
    @ResponseBody
    @GetMapping("/today/{empId}")
    public Map<String, String> getToday(@PathVariable String empId) {
        String today = LocalDate.now().toString();
        Attendance att = repo.findByEmpIdAndDate(empId, today);
        Map<String, String> map = new LinkedHashMap<>();

        if (att == null) {
            map.put("status",     "Not Marked");
            map.put("punchIn",    "");
            map.put("punchOut",   "");
            map.put("onBreak",    "false");
            map.put("breakTaken", "false");
            map.put("totalHours", "0");
        } else {
            map.put("status",     att.getStatus());
            map.put("punchIn",    att.getPunchIn()  != null ? att.getPunchIn().toString()  : "");
            map.put("punchOut",   att.getPunchOut() != null ? att.getPunchOut().toString() : "");
            map.put("onBreak",    String.valueOf(att.isOnBreak()));
            map.put("breakTaken", att.getBreakStart() != null ? "true" : "false");
            map.put("totalHours", String.valueOf(att.getTotalHours()));
        }
        return map;
    }

    // ✅ Admin Logs — empId ki jagah naam aayega
    @ResponseBody
    @GetMapping("/logs")
    public List<Map<String, String>> getAllLogs() {
        List<Attendance> all = repo.findAll();
        List<Map<String, String>> logs = new ArrayList<>();
        int id = 1;

        for (Attendance att : all) {

            // ✅ empId se naam fetch karo
            Employee emp = empRepo.findByEmpId(att.getEmpId());
            String name = (emp != null) ? emp.getName() : att.getEmpId();

            // Punch In log
            if (att.getPunchIn() != null) {
                Map<String, String> log = new LinkedHashMap<>();
                log.put("id",          String.valueOf(id++));
                log.put("user",        name); // ✅ naam
                log.put("activity",    "login");
                log.put("description", name + " punched in at " + att.getPunchIn());
                log.put("date",        att.getDate());
                log.put("time",        att.getPunchIn().toString());
                logs.add(log);
            }

            // Break log
            if (att.getBreakStart() != null) {
                Map<String, String> log = new LinkedHashMap<>();
                log.put("id",          String.valueOf(id++));
                log.put("user",        name); // ✅ naam
                log.put("activity",    "approve");
                log.put("description", name + " break started at " + att.getBreakStart()
                                       + (att.getBreakEnd() != null
                                           ? ", ended at " + att.getBreakEnd()
                                           : " (ongoing)"));
                log.put("date",        att.getDate());
                log.put("time",        att.getBreakStart().toString());
                logs.add(log);
            }

            // Punch Out log
            if (att.getPunchOut() != null) {
                Map<String, String> log = new LinkedHashMap<>();
                log.put("id",          String.valueOf(id++));
                log.put("user",        name); // ✅ naam
                log.put("activity",    "update");
                log.put("description", name + " punched out at " + att.getPunchOut()
                                       + " | Total: " + att.getTotalHours()
                                       + " hrs | Status: " + att.getStatus());
                log.put("date",        att.getDate());
                log.put("time",        att.getPunchOut().toString());
                logs.add(log);
            }
        }
        return logs;
    }
}