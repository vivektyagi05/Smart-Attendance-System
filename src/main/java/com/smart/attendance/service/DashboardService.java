package com.smart.attendance.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.smart.attendance.repository.*;
import com.smart.attendance.entity.*;
import com.smart.attendance.dto.DashboardResponse;

import java.util.Optional;

@Service
public class DashboardService {

    @Autowired private UserRepository userRepo;
    @Autowired private AttendanceRepository attendanceRepo;
    @Autowired private TaskRepository taskRepo;
    @Autowired private EventRepository eventRepo;
    @Autowired private SalaryRepository salaryRepo;
    @Autowired private PerformanceRepository performanceRepo;

    public DashboardResponse getDashboard(Long userId) {

        User user = userRepo.findById(userId).orElseThrow();

        DashboardResponse res = new DashboardResponse();

        res.setFullName(user.getFirstName() + " " + user.getLastName());

        Optional<Attendance> today = attendanceRepo.findTodayAttendance(userId);

        res.setCheckInTime(
            today.map(a -> a.getCheckIn().toString())
                 .orElse("Not Checked In")
        );

        res.setTodayTasks(taskRepo.findTop2ByUserIdOrderByDueDateAsc(userId));

        res.setEvents(eventRepo.findTop2ByOrderByEventDateAsc());

        Salary sal = salaryRepo.findByUserId(userId);
        res.setSalary(sal.getAmount());
        res.setBonus(sal.getBonus());
        res.setNextPayment(sal.getNextPayment().toString());

        Performance p = performanceRepo.findByUserId(userId);
        res.setPerformanceScore(p.getScore());

        return res;
    }
}