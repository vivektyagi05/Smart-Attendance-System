package com.smart.attendance.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.Productivity;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.ProductivityRepository;
@Service
public class ReportService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ProductivityRepository productivityRepository;

    // ✅ SYSTEM LEVEL: Average Attendance %
    public double calculateAverageAttendance() {
        long total = attendanceRepository.count();
        long present = attendanceRepository.countByStatus("PRESENT");
        return total == 0 ? 0 : (present * 100.0) / total;
    }

   // ✅ SYSTEM LEVEL: Average Productivity
    public double calculateAverageProductivity() {

        Double avg = productivityRepository.findAverageProductivity();

        // ⛑️ NULL SAFE
        return avg == null ? 0.0 : avg;
    }


    // ✅ USER LEVEL: Attendance Summary
    public Map<String, Object> attendanceSummary(long userId) {

        List<Attendance> list =
                attendanceRepository.findByUserId(userId);

        long presentDays = list.stream()
                .filter(a -> "PRESENT".equals(a.getStatus()))
                .count();
                
        double avgProductivity =
            Optional.ofNullable(productivityRepository
                    .findAverageProductivity())
                    .orElse(0.0);
        int totalDays = list.size();
        int percentage =
                totalDays == 0 ? 0 : (int) ((presentDays * 100) / totalDays);

        Map<String, Object> result = new HashMap<>();
        result.put("totalDays", totalDays);
        result.put("presentDays", presentDays);
        result.put("attendancePercentage", percentage);
        result.put("avgProductivity", avgProductivity);

        return result;
    }

    // ✅ USER LEVEL: Productivity Summary
    public Map<String, Object> productivitySummary(long userId) {

        List<Productivity> list =
                productivityRepository.findByUserId(userId);

        int avg = (int) list.stream()
                .mapToInt(Productivity::getProductivityScore)
                .average()
                .orElse(0);

        int max = list.stream()
                .mapToInt(Productivity::getProductivityScore)
                .max()
                .orElse(0);

        int min = list.stream()
                .mapToInt(Productivity::getProductivityScore)
                .min()
                .orElse(0);

        Map<String, Object> result = new HashMap<>();
        result.put("averageScore", avg);
        result.put("bestScore", max);
        result.put("worstScore", min);

        return result;
    }

    // ✅ USER LEVEL: Attendance Chart
    public List<Map<String, Object>> getUserAttendanceChart(long userId) {

        return attendanceRepository.findByUserId(userId)
                .stream()
                .map(a -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", a.getDate().toString());
                    map.put("status", a.getStatus());
                    return map;
                })
                .toList();
    }

    // ✅ USER LEVEL: Productivity Chart
    public List<Map<String, Object>> getUserProductivityChart(long userId) {

        return productivityRepository.findByUserId(userId)
                .stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", p.getDate().toString());
                    map.put("score", p.getProductivityScore());
                    return map;
                })
                .toList();
    }

    public Map<String, Object> monthlyAttendanceReport(
            long userId, int month, int year) {

        List<Attendance> list =
                attendanceRepository.findMonthlyAttendance(userId, month, year);

        long present = list.stream()
                .filter(a -> "PRESENT".equals(a.getStatus()))
                .count();

        long absent = list.stream()
                .filter(a -> "ABSENT".equals(a.getStatus()))
                .count();

        int total = list.size();
        int percentage = total == 0 ? 0 : (int)((present * 100) / total);

        return Map.of(
            "month", month,
            "year", year,
            "totalDays", total,
            "presentDays", present,
            "absentDays", absent,
            "attendancePercentage", percentage
        );
    }

    public Map<String, Object> monthlyProductivityReport(
            long userId, int month, int year) {

        List<Productivity> list =
                productivityRepository.findMonthlyProductivity(userId, month, year);

        int avg = (int) list.stream()
                .mapToInt(Productivity::getProductivityScore)
                .average()
                .orElse(0);

        int max = list.stream()
                .mapToInt(Productivity::getProductivityScore)
                .max()
                .orElse(0);

        int min = list.stream()
                .mapToInt(Productivity::getProductivityScore)
                .min()
                .orElse(0);

        return Map.of(
            "month", month,
            "year", year,
            "averageScore", avg,
            "bestScore", max,
            "worstScore", min
        );
    }

    public Map<String, Object> monthlyUserReport(
            long userId,
            int month,
            int year
    ) {
        long present =
                attendanceRepository.countPresentInMonth(userId, month, year);

        long total =
                attendanceRepository.countTotalInMonth(userId, month, year);

        long absent = total - present;

        int attendancePercentage =
                total == 0 ? 0 : (int) ((present * 100) / total);

        Double avgProductivity =
                productivityRepository
                        .findMonthlyAverageProductivity(userId, month, year);

        Map<String, Object> report = new HashMap<>();
        report.put("month", year + "-" + String.format("%02d", month));
        report.put("totalDays", total);
        report.put("presentDays", present);
        report.put("absentDays", absent);
        report.put("attendancePercentage", attendancePercentage);
        report.put("averageProductivity",
                avgProductivity == null ? 0 : avgProductivity.intValue());

        return report;
    }

        // 🔹 Monthly productivity trend
        public List<Map<String, Object>> getMonthlyProductivityTrend() {

        return productivityRepository
                .monthlyProductivity()
                .stream()
                .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("month", obj[0]);
                        map.put("averageScore", obj[1]);
                        return map;
                })
                .toList();
        }

        public List<Map<String, Object>> attendanceTrendLast30Days() {

        LocalDate start = LocalDate.now().minusDays(29);

        return attendanceRepository
                .countPresentGroupByDate(start)
                .stream()
                .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("date", obj[0].toString());
                        map.put("count", obj[1]);
                        return map;
                })
                .toList();
        }



}
