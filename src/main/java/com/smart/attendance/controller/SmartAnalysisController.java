package com.smart.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.service.SmartAnalysisService;

@RestController
@RequestMapping("/api/smart")
public class SmartAnalysisController {

    @Autowired
    private SmartAnalysisService smartAnalysisService;

    @GetMapping("/analyze")
    public String analyze(@RequestParam int userId) {
        return smartAnalysisService.analyzeUserPerformance(userId);
    }
}

