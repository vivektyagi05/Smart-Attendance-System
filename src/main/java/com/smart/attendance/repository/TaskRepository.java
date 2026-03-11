package com.smart.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.attendance.model.Task;

public interface TaskRepository
        extends JpaRepository<Task,Long>{

    List<Task> findByEmployeeId(Long empId);

    List<Task> findByEmployeeIdAndDueDate(
            Long empId, LocalDate date);

    long countByEmployeeIdAndStatus(
            Long empId,String status);

    long countByEmployeeId(Long employeeId);

}
