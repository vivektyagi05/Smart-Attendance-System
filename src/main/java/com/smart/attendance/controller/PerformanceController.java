package com.smart.attendance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.smart.attendance.model.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.PerformanceService;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired private PerformanceService performanceService;
    @Autowired private UserRepository userRepository;

    // ===============================
    // DASHBOARD SUMMARY
    // ===============================
    @GetMapping("/summary")
    public Map<String,Object> summary(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        return performanceService.summary(user);
    }

    // ===============================
    // WEEKLY ATTENDANCE CHART
    // ===============================
    @GetMapping("/weekly-attendance")
    public List<Map<String,Object>> weekly(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        return performanceService.weeklyAttendance(user);
    }
}
