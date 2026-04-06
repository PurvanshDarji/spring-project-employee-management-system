package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Feedback;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    
    // Employee feedbacks
    List<Feedback> findByEmpIdOrderByIdDesc(String empId);
    
    // Admin views
    @Query("SELECT f FROM Feedback f ORDER BY f.id DESC")
    List<Feedback> findAllByOrderByIdDesc();
    
    @Query("SELECT f FROM Feedback f ORDER BY f.id DESC")
    List<Feedback> findTop10ByOrderByIdDesc();
    
    @Query("SELECT COUNT(f) FROM Feedback f")
    long countTotalFeedbacks();
    
    // 🔥 FIXED: Native query for String department ✅
    @Query(value = "SELECT department, COUNT(*) as count FROM feedback GROUP BY department ORDER BY count DESC", nativeQuery = true)
    List<Object[]> countFeedbacksByDepartment();
    
    // Department filtering
    List<Feedback> findByDepartmentOrderByIdDesc(String department);
    
    // Recent feedbacks
    @Query("SELECT f FROM Feedback f WHERE f.id > :lastId ORDER BY f.id DESC")
    List<Feedback> findRecentFeedbacks(@Param("lastId") Integer lastId);
}