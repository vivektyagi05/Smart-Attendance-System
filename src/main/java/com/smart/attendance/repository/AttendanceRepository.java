package com.smart.attendance.repository;

import com.smart.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a WHERE a.userId = :userId AND a.date = CURRENT_DATE")
    Optional<Attendance> findTodayAttendance(@Param("userId") Long userId);

    Attendance findByUserIdAndDate(Long userId, LocalDate date);

    List<Attendance> findByUserId(Long userId);
    
}