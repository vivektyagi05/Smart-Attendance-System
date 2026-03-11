package com.smart.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.attendance.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // ================= USER LEVEL =================

    List<Attendance> findByUserId(Long userId);

    List<Attendance> findByDate(LocalDate date );

    boolean existsByUserIdAndDate(Long userId, LocalDate date);



    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    long countByDateAndStatus(LocalDate date, String status);

    List<Attendance> findByEmployeeIdOrderByDateDesc(Long employeeId);

    long countByEmployeeIdAndStatus(Long empId, String status);

    long countByEmployeeId(Long employeeId);

    
     @Query("""
        SELECT a
        FROM Attendance a
        WHERE a.employeeId = :empId
        AND a.date >= :from
        AND a.date <= :to
    """)

    List<Attendance> findByEmployeeIdAndDateRange(
        @Param("empId") Long empId, 
        @Param("from") LocalDate from, 
        @Param("to") LocalDate to
    );

    // ================= COUNT HELPERS =================

    long countByStatus(String status);

    // Optional (future use – admin stats)
    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.status = 'PRESENT'
    """)
    
    long countPresent();

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.status = 'ABSENT'
    """)
    long countAbsent();

    @Query("""
        SELECT a.userId
        FROM Attendance a
        WHERE a.date = :date
    """)
    List<Long> findUserIdsMarkedToday(LocalDate date);

    @Query("""
        SELECT a
        FROM Attendance a
        WHERE a.userId = :userId
        AND MONTH(a.date) = :month
        AND YEAR(a.date) = :year
    """)
    List<Attendance> findMonthlyAttendance(
            long userId,
            int month,
            int year
    );

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.userId = :userId
        AND MONTH(a.date) = :month
        AND YEAR(a.date) = :year
        AND a.status = 'PRESENT'
    """)
    long countPresentInMonth(
            long userId,
            int month,
            int year
    );

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.userId = :userId
        AND MONTH(a.date) = :month
        AND YEAR(a.date) = :year
    """)
    long countTotalInMonth(
            long userId,
            int month,
            int year
    );

    
    @Query("""
        SELECT a.date, COUNT(a)
        FROM Attendance a
        WHERE a.status = 'PRESENT'
          AND a.date >= :startDate
        GROUP BY a.date
        ORDER BY a.date
    """)
    List<Object[]> countPresentGroupByDate(
            @Param("startDate") LocalDate startDate
    );


    @Query("""
        SELECT a FROM Attendance a
        WHERE a.employeeId = :empId
        AND a.date BETWEEN :from AND :to
        AND (:status IS NULL OR a.status = :status)
        ORDER BY a.date DESC
    """)
    List<Attendance> filterAttendance(
        @Param("empId") Long empId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to,
        @Param("status") String status
    );

    @Query("""
        SELECT AVG(a.workingMinutes)
        FROM Attendance a
        WHERE a.employeeId = :empId
        AND a.status = 'PRESENT'
    """)
    Integer averageWorkingMinutes(@Param("empId") Long empId);

    List<Attendance> findByEmployeeId(Long employeeId);

    List<Attendance> findByEmployeeIdAndDateBetween(
            Long employeeId,
            LocalDate from,
            LocalDate to
    );

    List<Attendance> findByEmployeeIdAndDateBetweenAndStatus(
            Long employeeId,
            LocalDate from,
            LocalDate to,
            String status
    );

    @Query("""
        SELECT MONTH(a.date) AS month, COUNT(a) AS count
        FROM Attendance a
        WHERE a.userId = :userId
        AND YEAR(a.date) = :year
        GROUP BY MONTH(a.date)
    """)    
    List<Object[]> getMonthlyTrend(
        @Param("userId") Long userId, 
        @Param("year") int year
    );

    @Query("""
    SELECT COUNT(a)
    FROM Attendance a
    WHERE a.employeeId = :empId
    """)
    long countTotalDays(Long empId);

    @Query("""
        SELECT a FROM Attendance a 
        WHERE a.userId = :userId 
        AND a.date >= :startDate
    """)    
    List<Attendance> last7Days(
        @Param("userId") Long userId, 
        @Param("startDate") LocalDate startDate
    );
}
