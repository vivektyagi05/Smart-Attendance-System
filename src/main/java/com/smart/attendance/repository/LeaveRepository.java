package com.smart.attendance.repository;

import com.smart.attendance.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    // 🔹 Total days by Leave Type
    @Query("SELECT COALESCE(SUM(l.days),0) FROM Leave l WHERE l.leaveType = :leaveType")
    int getTotalByType(String leaveType);

    // 🔹 Total days by Status
    @Query("SELECT COALESCE(SUM(l.days),0) FROM Leave l WHERE l.status = :status")
    int getTotalByStatus(String status);

    List<Leave> findTop10ByOrderByAppliedDateDesc();

    List<Leave> findTop10ByStatusOrderByAppliedDateDesc(String status);
}