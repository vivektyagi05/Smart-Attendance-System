package com.smart.attendance.service;

import com.smart.attendance.entity.bankOtp;
import com.smart.attendance.repository.bankOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class bankOtpService {

    @Autowired
    private bankOtpRepository repo;

    @Autowired
    private EmailService emailService; // already hai tumhare project me

    public void sendOtp(String email) {

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        bankOtp entity = new bankOtp();
        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        entity.setVerified(false);

        repo.save(entity);

        // Send Email
        emailService.sendEmail(email, "OTP Verification", "Your OTP is: " + otp);
    }

    public boolean verifyOtp(String email, String otpInput) {

        bankOtp otp = repo.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        System.out.println("Stored OTP: " + otp.getOtp());
        System.out.println("User OTP: " + otpInput);

        if (!otp.getOtp().equals(otpInput)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (LocalDateTime.now().isAfter(otp.getExpiryTime())) {
            throw new RuntimeException("OTP expired");
        }

        otp.setVerified(true);
        repo.save(otp);

        return true;
    }
}