package com.smart.attendance.controller;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;



    // ✅ USER: mark attendance (check-in)
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(
            @AuthenticationPrincipal String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Attendance attendance =
                attendanceService.markAttendance(user.getId());

        return ResponseEntity.ok(
                Map.of(
                        "message", "Attendance marked successfully",
                        "date", attendance.getDate()
                )
        );
    }


    // ✅ USER: today status
    @GetMapping("/status")
    public Map<String,Object> status(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email)
                .orElseThrow();

        return attendanceService.todayStatus(user.getId());
    }

    // ===============================
    // CHECK-IN
    // ===============================
    @PostMapping("/check-in")
    public Map<String,Object> checkIn(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        Attendance a=
            attendanceService.checkIn(user.getId());

        return Map.of(
            "message","Checked in",
            "time",a.getCheckIn()
        );
    }

    // ===============================
    // CHECK-OUT
    // ===============================
    @PostMapping("/check-out")
    public Map<String,Object> checkOut(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        Attendance a=
            attendanceService.checkOut(user.getId());

        return Map.of(
            "message","Checked out",
            "workingMinutes",a.getWorkingMinutes()
        );
    }

     // ===============================
    // TODAY STATUS
    // ===============================
    @GetMapping("/today")
    public Map<String,Object> today(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        return attendanceService.todayStatus(user.getId());
    }

    // ===============================
    // HISTORY
    // ===============================
    @GetMapping("/history")
    public List<Attendance> history(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        return attendanceService.history(user.getId());
    }

    @GetMapping("/last-7-days/{userId}")
    public List<Attendance> getTrend(@PathVariable Long userId) {

        LocalDate startDate =
            LocalDate.now().minusDays(7);

        return attendanceRepository.last7Days(userId, startDate);
    }


    
}
