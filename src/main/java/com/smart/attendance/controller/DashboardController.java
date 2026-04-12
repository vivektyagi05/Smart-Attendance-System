package com.smart.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.dto.DashboardResponse;
import com.smart.attendance.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private DashboardService service;
    @Autowired private DashboardService dashboardService;

    // Isko fix karein: Path variable ki jagah Token se ID lein
    @GetMapping("/user-data") 
    public ResponseEntity<DashboardResponse> getDashboardData(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(dashboardService.getDashboard(userId));
    }
}