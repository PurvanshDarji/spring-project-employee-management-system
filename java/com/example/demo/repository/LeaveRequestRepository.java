package com.example.demo.repository;

import com.example.demo.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    // 🔥 1. PENDING LEAVES FOR ADMIN TABLE (Most Important)
    List<LeaveRequest> findByStatusOrderByIdDesc(String status);

    // 🔥 2. COUNT FOR DASHBOARD STATS
    long countByStatus(String status);

    // 🔥 3. EMPLOYEE LEAVES HISTORY
 // LeaveRequestRepository.java mein ye method add karo:
    List<LeaveRequest> findByEmpIdAndFromDateAndToDate(String empId, String fromDate, String toDate);
    List<LeaveRequest> findByEmpIdOrderByIdDesc(String empId);
    List<LeaveRequest> findByEmpId(String empId);

    // 🔥 4. SEARCH BY EMP ID OR NAME (Admin Search)
    @Query("SELECT l FROM LeaveRequest l WHERE " +
           "LOWER(l.empId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(l.empName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<LeaveRequest> findByEmpIdContainingOrEmpNameContaining(
            @Param("search") String empId, 
            @Param("search") String empName);

    // 🔥 5. ALL LEAVES BY STATUS (Analytics)
    List<LeaveRequest> findByStatus(String status);

    // 🔥 6. RECENT LEAVES (Last 10)
    List<LeaveRequest> findTop10ByOrderByIdDesc();

    // 🔥 7. LEAVES BY DEPARTMENT (Admin Filter)
    List<LeaveRequest> findByDepartmentOrderByIdDesc(String department);
}