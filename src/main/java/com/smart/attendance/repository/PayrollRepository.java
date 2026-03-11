package com.smart.attendance.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.smart.attendance.model.Payroll;


@Repository
public interface PayrollRepository
        extends JpaRepository<Payroll,Long>{

    Payroll findTopByEmployeeIdOrderByIdDesc(Long empId);

    @Query("""
    SELECT p.status
    FROM Payroll p
    WHERE p.employeeId=:empId
    AND p.month = FUNCTION('MONTH', CURRENT_DATE)
    ORDER BY p.id DESC
    LIMIT 1
    """)
    String currentMonthStatus(Long empId);

}
