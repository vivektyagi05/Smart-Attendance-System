package com.smart.attendance.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Productivity;
import com.smart.attendance.repository.ProductivityRepository;

@Service
public class ProductivityService {

    private static final int EXPECTED_HOURS = 8;

    @Autowired
    private ProductivityRepository productivityRepository;

    public String addProductivity(long userId, int hoursWorked) {

        LocalDate today = LocalDate.now();

        Productivity existing =
                productivityRepository.findByUserIdAndDate(userId, today);

        if (existing != null) {
            return "Productivity already submitted for today";
        }

        int score = (hoursWorked * 100) / EXPECTED_HOURS;
        if (score > 100) score = 100;

        Productivity p = new Productivity();
        p.setUserId(userId);
        p.setDate(today);
        p.setHoursWorked(hoursWorked);
        p.setProductivityScore(score);

        productivityRepository.save(p);

        return "Productivity submitted successfully. Score = " + score;
    }

}
