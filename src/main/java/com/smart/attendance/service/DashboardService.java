package com.smart.attendance.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.dto.DashboardResponse;
import com.smart.attendance.entity.Attendance;
import com.smart.attendance.entity.Performance;
import com.smart.attendance.entity.Salary;
import com.smart.attendance.entity.User;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.EventRepository;
import com.smart.attendance.repository.PerformanceRepository;
import com.smart.attendance.repository.SalaryRepository;
import com.smart.attendance.repository.TaskRepository;
import com.smart.attendance.repository.UserRepository;

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

        // 1. Basic Info
        res.setFullName(user.getFirstName() + " " + user.getLastName());

        // 2. Attendance (Aaj ki entry uthao)
        Optional<Attendance> today = attendanceRepo.findTodayAttendance(userId);
        res.setCheckInTime(today.map(a -> a.getCheckIn().toString()).orElse("Not Checked In"));

        // 3. Tasks (Sirf top 2 pending tasks)
        res.setTodayTasks(taskRepo.findTop2ByUserIdOrderByDueDateAsc(userId));

        // 4. Events
        res.setEvents(eventRepo.findTop2ByOrderByEventDateAsc());

        // 5. Salary & Performance
        Salary sal = salaryRepo.findByUserId(userId);
        res.setSalary(sal.getAmount());
        res.setBonus(sal.getBonus());
        res.setNextPayment(sal.getNextPayment().toString());

        Performance p = performanceRepo.findByUserId(userId);
        res.setPerformanceScore(p.getScore());

        res.setCreatedAt(user.getCreatedAt().toString());

        return res;
    }

}