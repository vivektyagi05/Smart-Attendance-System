package com.smart.attendance.controller;

import com.smart.attendance.service.ActivityLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin("*")
public class ActivityLogController {

    @Autowired
    private ActivityLogService service;

    @GetMapping
    public ResponseEntity<?> getLogs(Authentication auth) {
        return ResponseEntity.ok(service.getLogs(auth.getName()));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication auth) {
        service.log(auth.getName(), "Logged Out");
        return ResponseEntity.ok("Logged out");
    }
}