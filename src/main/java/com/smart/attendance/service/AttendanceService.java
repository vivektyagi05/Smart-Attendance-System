package com.smart.attendance.service;
import com.smart.attendance.entity.Attendance;
import com.smart.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;



@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository repo;

    public Attendance checkIn(Long userId) {

        LocalDate today = LocalDate.now();

        Attendance existing = repo.findByUserIdAndDate(userId, today);
        if (existing != null) {
            throw new RuntimeException("Already checked in");
        }

        Attendance att = new Attendance();
        att.setUserId(userId);
        att.setDate(today);
        att.setCheckIn(LocalTime.now());
        att.setStatus("Present");

        return repo.save(att);
    }

    public Attendance checkOut(Long userId) {

        LocalDate today = LocalDate.now();

        Attendance att = repo.findByUserIdAndDate(userId, today);

        if (att == null) {
            throw new RuntimeException("Check-in first");
        }

        att.setCheckOut(LocalTime.now());

        Duration duration = Duration.between(att.getCheckIn(), att.getCheckOut());
        long h = duration.toHours();
        long m = duration.toMinutes() % 60;

        att.setWorkingHours(h + "h " + m + "m");

        return repo.save(att);
    }

    public List<Attendance> getUserAttendance(Long userId) {
        return repo.findByUserId(userId);
    }
}