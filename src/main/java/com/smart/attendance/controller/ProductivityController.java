package com.smart.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.service.ProductivityService;

@RestController
@RequestMapping("/api/productivity")
@PreAuthorize("hasAuthority('USER')")
public class ProductivityController {

    @Autowired
    private ProductivityService productivityService;

    @PostMapping("/add")
    public String addProductivity(
            @RequestParam long userId,
            @RequestParam int hoursWorked
    ) {
        return productivityService.addProductivity(userId, hoursWorked);
    }

}
