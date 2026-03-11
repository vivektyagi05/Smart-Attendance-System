package com.smart.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart.attendance.model.Productivity;

public interface ProductivityRepository extends JpaRepository<Productivity, Long> {


    List<Productivity> findByUserId(long userId);
    Productivity findByUserIdAndDate(long userId, java.time.LocalDate date);
    @Query("""
        SELECT AVG(p.productivityScore)
        FROM Productivity p
    """)
    Double findAverageProductivity();

    @Query("""
        SELECT p
        FROM Productivity p
        WHERE p.userId = :userId
        AND MONTH(p.date) = :month
        AND YEAR(p.date) = :year
    """)
    List<Productivity> findMonthlyProductivity(
            long userId,
            int month,
            int year
    );

    @Query("""
        SELECT AVG(p.productivityScore)
        FROM Productivity p
        WHERE p.userId = :userId
        AND MONTH(p.date) = :month
        AND YEAR(p.date) = :year
    """)
    Double findMonthlyAverageProductivity(
            long userId,
            int month,
            int year
    );

    @Query("""
    SELECT MONTH(p.date), AVG(p.productivityScore)
    FROM Productivity p
    GROUP BY MONTH(p.date)
    ORDER BY MONTH(p.date)
    """)
    List<Object[]> monthlyProductivity();

    @Query("""
        SELECT AVG(p.score)
        FROM Productivity p
        WHERE p.employeeId = :empId
    """)
    Double getAverageScore(Long empId);

    @Query("""
    SELECT AVG(p.productivityScore)
    FROM Productivity p
    WHERE p.employeeId=:empId
    """)
    Double average(Long empId);


}
    