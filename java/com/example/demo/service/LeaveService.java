package com.example.demo.service;

import com.example.demo.entity.LeaveRequest;
import com.example.demo.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    // 🔥 DASHBOARD STATS - MAP RETURN (No DTO)
    public Map<String, Long> getLeaveStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("pending", leaveRequestRepository.countByStatus("Pending"));
        stats.put("approved", leaveRequestRepository.countByStatus("Approved"));
        stats.put("rejected", leaveRequestRepository.countByStatus("Rejected"));
        stats.put("total", leaveRequestRepository.count());
        return stats;
    }

    // 🔥 PENDING LEAVES FOR TABLE
    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatusOrderByIdDesc("Pending");
    }

    // 🔥 APPROVE
    public void approveLeave(int id) {
        LeaveRequest leave = leaveRequestRepository.findById(id).orElse(null);
        if (leave != null && "Pending".equals(leave.getStatus())) {
            leave.setStatus("Approved");
            leaveRequestRepository.save(leave);
        }
    }

    // 🔥 REJECT
    public void rejectLeave(int id, String reason) {
        LeaveRequest leave = leaveRequestRepository.findById(id).orElse(null);
        if (leave != null && "Pending".equals(leave.getStatus())) {
            leave.setStatus("Rejected");
            leave.setReason(reason);
            leaveRequestRepository.save(leave);
        }
    }

    // 🔥 EMPLOYEE SUBMIT
    public LeaveRequest submitLeave(String empId, String empName, String dept, 
                                  String leaveType, String from, String to, String reason) {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmpId(empId);
        leave.setEmpName(empName);
        leave.setDepartment(dept);
        leave.setLeaveType(leaveType);
        leave.setFromDate(from);
        leave.setToDate(to);
        leave.setReason(reason);
        leave.setStatus("Pending");
        return leaveRequestRepository.save(leave);
    }
}