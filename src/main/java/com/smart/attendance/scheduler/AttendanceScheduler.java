package com.smart.attendance.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.UserRepository;

@Component
public class AttendanceScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // 🕛 Every day at 12:00 AM
    @Scheduled(cron = "0 0 0 * * ?")
    public void markAbsentUsers() {

        LocalDate today = LocalDate.now().minusDays(1);

        // 1️⃣ All active users
        List<User> users = userRepository.findByActiveTrue();

        // 2️⃣ Users who already marked attendance
        List<Long> markedUserIds =
                attendanceRepository.findUserIdsMarkedToday(today);

        // 3️⃣ Mark ABSENT for remaining users
        for (User user : users) {

            if (!markedUserIds.contains(user.getId())) {

                Attendance attendance = new Attendance();
                attendance.setUserId(user.getId());
                attendance.setDate(today);
                attendance.setStatus("ABSENT");

                attendanceRepository.save(attendance);
            }
        }
        

        System.out.println("✅ Auto-ABSENT marked for date: " + today);
    }
}
