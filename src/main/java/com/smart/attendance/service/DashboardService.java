package com.smart.attendance.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.Notification;
import com.smart.attendance.model.Task;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.EmployeeRepository;
import com.smart.attendance.repository.LeaveRepository;
import com.smart.attendance.repository.NotificationRepository;
import com.smart.attendance.repository.ProductivityRepository;
import com.smart.attendance.repository.TaskRepository;

@Service
public class DashboardService {

    @Autowired private AttendanceRepository attendanceRepo;
    @Autowired private TaskRepository taskRepo;
    @Autowired private LeaveRepository leaveRepo;
    @Autowired private NotificationRepository notificationRepo;
    @Autowired private ProductivityRepository productivityRepo;
    @Autowired private EmployeeRepository employeeRepo;


    public Map<String,Object> dashboard(Long empId){

        LocalDate today = LocalDate.now();

        // =============================
        // TODAY ATTENDANCE
        // =============================
        Attendance todayAttendance =
            attendanceRepo
                .findByEmployeeIdAndDate(empId,today)
                .orElse(null);

        String checkIn =
            todayAttendance==null || todayAttendance.getCheckIn()==null
            ? "--:--"
            : todayAttendance.getCheckIn().toString();

        String checkOut =
            todayAttendance==null || todayAttendance.getCheckOut()==null
            ? "--:--"
            : todayAttendance.getCheckOut().toString();

        Integer working =
            todayAttendance==null ? 0 :
            (todayAttendance.getWorkingMinutes()==null
                ? 0 : todayAttendance.getWorkingMinutes());

        // =============================
        // PERFORMANCE
        // =============================
        long totalDays =
            attendanceRepo.countByEmployeeId(empId);

        long presentDays =
            attendanceRepo.countByEmployeeIdAndStatus(
                empId,"PRESENT");

        int attendanceRate =
            totalDays==0?0:
            (int)((presentDays*100)/totalDays);

        Double productivity =
            productivityRepo.average(empId);

        // =============================
        // TODAY TASKS
        // =============================
        List<Task> todayTasks =
            taskRepo.findByEmployeeIdAndDueDate(empId,today);

        // =============================
        // NOTIFICATIONS
        // =============================
        List<Notification> notifications =
            notificationRepo
            .findTop5ByEmployeeIdOrderByCreatedAtDesc(empId);

        // =============================
        // PENDING COUNTS
        // =============================
        long pendingTasks =
            taskRepo.countByEmployeeIdAndStatus(empId,"TODO");

        String empCode = employeeRepo.findById(empId)
                .map(e -> e.getEmployeeCode())
                .orElse(null);

        long leaveUsed = leaveRepo.currentMonthLeaveCount(empCode);
        // =============================
        // WEEKLY ATTENDANCE
        // =============================
        List<Attendance> weekly = attendanceRepo.findByEmployeeId(empId);

        return Map.of(
            "checkIn",checkIn,
            "checkOut",checkOut,
            "workingHours",working,
            "attendanceRate",attendanceRate,
            "productivityScore",
                productivity==null?0:productivity,
            "todayTasks",todayTasks,
            "notifications",notifications,
            "pendingTasks",pendingTasks,
            "leaveUsed",leaveUsed,
            "weekly",weekly
        );
    }
}
