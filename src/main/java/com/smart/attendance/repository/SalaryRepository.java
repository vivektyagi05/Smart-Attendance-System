package com.smart.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.attendance.entity.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    Salary findByUserId(Long userId);
}