package com.smart.attendance.controller;

import com.smart.attendance.entity.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.bankOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin("*")
public class OtpController {

    @Autowired private bankOtpService otpService;
    @Autowired private UserRepository userRepo;

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(Authentication authentication) {

        String email = authentication.getName();

        otpService.sendOtp(email);

        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(Authentication authentication,
                                       @RequestBody Map<String, String> req) {

        String email = authentication.getName();
        String otp = req.get("otp");

        otpService.verifyOtp(email, otp);

        return ResponseEntity.ok("OTP verified");
    }
}