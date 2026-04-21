package com.smart.attendance.controller;

import com.smart.attendance.dto.BankRequest;
import com.smart.attendance.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.entity.User;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private BankService service;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/bank-details")
    public ResponseEntity<?> getBank(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            return ResponseEntity.ok(service.getBankDetails(user.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("No bank details");
        }
    }

    @PostMapping("/bank-details")
    public ResponseEntity<?> save(Authentication authentication,
                                @RequestBody BankRequest req) {

        String email = authentication.getName();

        User user = userRepo.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(service.saveOrUpdate(user.getId(), email, req));
    }

    @PutMapping("/bank-details")
    public ResponseEntity<?> update(Authentication authentication,
                                    @RequestBody BankRequest req) {

        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(service.saveOrUpdate(user.getId(), email, req));
    }
        
}