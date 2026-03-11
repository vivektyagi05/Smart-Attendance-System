package com.smart.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.model.User;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.ReportService;
import com.smart.attendance.service.SmartAnalysisService;


@RestController
@RequestMapping("/api/report/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private SmartAnalysisService smartAnalysisService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepo;


    // ---------------- DASHBOARD SUMMARY ----------------

    @GetMapping("/summary")
    public Map<String, Object> adminSummary() {
        return Map.of(
            "averageAttendance", reportService.calculateAverageAttendance(),
            "averageProductivity", reportService.calculateAverageProductivity()
        );
    }

    // ---------------- ATTENDANCE TREND ----------------

    @GetMapping("/attendance-trend")
    public List<Map<String, Object>> attendanceTrend() {
        return reportService.attendanceTrendLast30Days();
    }


    // ---------------- PRODUCTIVITY TREND ----------------

    @GetMapping("/productivity-trend")
    public List<Map<String, Object>> productivityTrend() {
        return reportService.getMonthlyProductivityTrend();
    }

    // ---------------- USER DASHBOARD ----------------

    @GetMapping("/user/dashboard")
    public Map<String, Object> adminUserDashboard(
            @RequestParam long userId) {

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("attendance", reportService.attendanceSummary(userId));
        dashboard.put("productivity", reportService.productivitySummary(userId));
        dashboard.put("remark", smartAnalysisService.analyzeUserPerformance(userId));
        return dashboard;
    }

    // ✅ ADMIN / USER: Attendance summary
    @GetMapping("/attendance/{userId}")
    public Map<String, Object> attendanceSummary(
            @PathVariable long userId) {

        return reportService.attendanceSummary(userId);
    }

    // ✅ ADMIN / USER: Productivity summary
    @GetMapping("/productivity/{userId}")
    public Map<String, Object> productivitySummary(
            @PathVariable long userId) {

        return reportService.productivitySummary(userId);
    }

    // ✅ ADMIN / USER: Attendance chart
    @GetMapping("/attendance/chart/{userId}")
    public List<Map<String, Object>> attendanceChart(
            @PathVariable long userId) {

        return reportService.getUserAttendanceChart(userId);
    }

    // ✅ ADMIN / USER: Productivity chart
    @GetMapping("/productivity/chart/{userId}")
    public List<Map<String, Object>> productivityChart(
            @PathVariable long userId) {

        return reportService.getUserProductivityChart(userId);
    }

    // ✅ USER / ADMIN: Monthly attendance report
    @GetMapping("/attendance/monthly")
    public Map<String, Object> monthlyAttendance(
            @RequestParam long userId,
            @RequestParam int month,
            @RequestParam int year) {

        return reportService.monthlyAttendanceReport(userId, month, year);
    }

    // ✅ USER / ADMIN: Monthly productivity report
    @GetMapping("/productivity/monthly")
    public Map<String, Object> monthlyProductivity(
            @RequestParam long userId,
            @RequestParam int month,
            @RequestParam int year) {

        return reportService.monthlyProductivityReport(userId, month, year);
    }

    // ✅ ADMIN / USER: Monthly report
    @GetMapping("/monthly/{userId}")
    public Map<String, Object> monthlyReport(
            @PathVariable long userId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return reportService.monthlyUserReport(userId, month, year);
    }

    @GetMapping("/user/monthly-attendance")
    public List<Object[]> monthlyTrend(@AuthenticationPrincipal String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use user.getId() and pass the current YEAR, not the month
        int currentYear = LocalDate.now().getYear();
        
        return attendanceRepo.getMonthlyTrend(user.getId(), currentYear);
    }


}
