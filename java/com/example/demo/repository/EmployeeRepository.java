package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import java.util.List;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer>{

    Employee findByPhoneAndPassword(String phone, String password);
    
    // 🔥 ADD THIS LINE:
    Employee findByPhone(String phone);
    
    @Query("SELECT COUNT(DISTINCT e.department) FROM Employee e")
    long countDepartments();
    Employee findByEmpId(String empId);
    List<Employee> findTop5ByOrderByIdDesc();
    
    @Query("""
    		SELECT e.department , COUNT(e)
    		FROM Employee e
    		GROUP BY e.department
    		""")
    		List<Object[]> countEmployeeByDepartment();

}