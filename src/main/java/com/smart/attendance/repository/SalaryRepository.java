package com.smart.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.attendance.entity.Salary;
import java.util.*;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    List<Salary> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Salary> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}