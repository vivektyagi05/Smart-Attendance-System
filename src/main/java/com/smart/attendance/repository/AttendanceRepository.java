package com.smart.attendance.repository;

import com.smart.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND CAST(a.checkIn AS date) = CURRENT_DATE")
    Optional<Attendance> findTodayAttendance(Long userId);
}