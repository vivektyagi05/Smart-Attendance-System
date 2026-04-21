package com.smart.attendance.repository;

import com.smart.attendance.entity.bankOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface bankOtpRepository extends JpaRepository<bankOtp, Long> {

    Optional<bankOtp> findTopByEmailOrderByIdDesc(String email);
}