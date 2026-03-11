package com.smart.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.Productivity;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.ProductivityRepository;

@Service
public class SmartAnalysisService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ProductivityRepository productivityRepository;

    public String analyzeUserPerformance(long userId) {

        List<Attendance> attendanceList =
                attendanceRepository.findAll()
                        .stream()
                        .filter(a -> a.getUserId() == userId)
                        .toList();

        List<Productivity> productivityList =
                productivityRepository.findAll()
                        .stream()
                        .filter(p -> p.getUserId() == userId)
                        .toList();

        if (attendanceList.isEmpty() || productivityList.isEmpty()) {
            return "Not enough data for analysis";
        }

        long presentDays = attendanceList.stream()
                .filter(a -> "Present".equalsIgnoreCase(a.getStatus()))
                .count();

        int totalDays = attendanceList.size();
        int attendancePercentage = (int) ((presentDays * 100) / totalDays);

        int avgProductivity = (int) productivityList.stream()
                .mapToInt(Productivity::getProductivityScore)
                .average()
                .orElse(0);

        // SMART RULES
        if (attendancePercentage < 70 && avgProductivity < 60) {
            return "Low attendance and productivity. Immediate improvement needed.";
        }

        if (attendancePercentage < 70) {
            return "Attendance is low. Try to be more regular.";
        }

        if (avgProductivity < 60) {
            return "Productivity is low. Focus on time management.";
        }

        if (attendancePercentage >= 90 && avgProductivity >= 80) {
            return "Excellent performance! Keep it up.";
        }

        return "Good performance. Keep improving.";
    }
}
