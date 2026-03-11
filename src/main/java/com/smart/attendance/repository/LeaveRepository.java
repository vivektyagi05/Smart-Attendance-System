package com.smart.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.attendance.model.LeaveRequest;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByStatus(String status);

    @Query("""
        SELECT COUNT(l)
        FROM LeaveRequest l
        WHERE l.employeeCode = :empCode
        AND l.status = 'APPROVED'
        AND FUNCTION('MONTH', l.fromDate) = FUNCTION('MONTH', CURRENT_DATE)
    """)
    long currentMonthLeaveCount(@Param("empCode") String empCode);


}
