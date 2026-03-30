package com.smart.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{userId}")
    public DashboardResponse getDashboard(@PathVariable Long userId){
        return service.getDashboard(userId);
    }
}