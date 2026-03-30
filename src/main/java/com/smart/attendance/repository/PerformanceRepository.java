package com.smart.attendance.repository;

import com.smart.attendance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    Performance findByUserId(Long userId);
}