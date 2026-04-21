package com.smart.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.attendance.entity.BankDetails;
import java.util.*;

public interface BankRepository extends JpaRepository<BankDetails, Long> {
    Optional<BankDetails> findByUserId(Long userId);
}