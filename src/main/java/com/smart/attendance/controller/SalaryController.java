package com.smart.attendance.controller;

import com.smart.attendance.service.SalaryService;
import com.smart.attendance.service.UserService;
import com.smart.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smart.attendance.entity.User;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api")
public class SalaryController {

    @Autowired
    private SalaryService service;
    @Autowired 
    private UserRepository userRepo;

    @GetMapping("/salary")
    public ResponseEntity<?> getSalary(Authentication authentication) {

        String email = authentication.getName(); // JWT se aaya

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(service.getSalary(user.getId()));
    }
    @GetMapping("/salary-history")
    public ResponseEntity<?> getHistory(Authentication auth) {

        String email = auth.getName();

        User user = userRepo.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(service.getSalaryHistory(user.getId()));
    }
}