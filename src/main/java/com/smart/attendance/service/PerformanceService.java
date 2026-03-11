package com.smart.attendance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.ProductivityRepository;
import com.smart.attendance.repository.TaskRepository;

@Service
public class PerformanceService {

    @Autowired private AttendanceRepository attendanceRepo;
    @Autowired private ProductivityRepository productivityRepo;
    @Autowired private TaskRepository taskRepo;

    // ===============================
    // PERFORMANCE SUMMARY (DASHBOARD)
    // ===============================
    public Map<String,Object> summary(User user){

        Long empId = user.getId();

        long present =
            attendanceRepo.countByEmployeeIdAndStatus(empId,"PRESENT");

        long total =
            attendanceRepo.countTotalDays(empId);

        double attendanceRate =
            total==0?0:(present*100.0)/total;

        Double productivity =
            productivityRepo.getAverageScore(empId);

        long totalTasks =
            taskRepo.countByEmployeeId(empId);

        long completedTasks =
            taskRepo.countByEmployeeIdAndStatus(empId,"DONE");

        double taskCompletion =
            totalTasks==0?0:(completedTasks*100.0)/totalTasks;

        return Map.of(
            "attendanceRate",round(attendanceRate),
            "productivityScore",productivity==null?0:round(productivity),
            "taskCompletion",round(taskCompletion)
        );
    }

    // ===============================
    // 7-DAY ATTENDANCE TREND
    // ===============================
    public List<Map<String,Object>> weeklyAttendance(User user){

        Long userId = user.getId();

        LocalDate sevenDaysAgo =
            LocalDate.now().minusDays(7);

        List<Attendance> rows =
            attendanceRepo.last7Days(userId, sevenDaysAgo);

        List<Map<String,Object>> list = new ArrayList<>();

        for(Attendance a : rows){

            list.add(
                Map.of(
                    "date", a.getDate(),
                    "status", a.getStatus()
                )
            );
        }

        return list;
    }


    private double round(double v){
        return Math.round(v*100.0)/100.0;
    }
}
