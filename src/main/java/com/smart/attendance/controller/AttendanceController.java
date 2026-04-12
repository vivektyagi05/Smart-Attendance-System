package com.smart.attendance.controller;

import com.smart.attendance.entity.Attendance;
import com.smart.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.entity.User;




@RestController
@RequestMapping("/api/attendance")
@CrossOrigin
public class AttendanceController {

    @Autowired
    private AttendanceService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/my")
    public List<Attendance> getMyAttendance(Authentication authentication) {

        String email = authentication.getName(); // JWT se aata hai

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getUserAttendance(user.getId());
    }

    @PostMapping("/checkin")
    public Attendance checkIn(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.checkIn(user.getId());
    }

    @PostMapping("/checkout")
    public Attendance checkOut(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.checkOut(user.getId());
    }

    @GetMapping("/user/{userId}")
    public List<Attendance> getAttendance(@PathVariable Long userId) {
        return service.getUserAttendance(userId);
    }
}