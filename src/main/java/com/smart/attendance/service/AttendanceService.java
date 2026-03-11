package com.smart.attendance.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.UserRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private AttendanceRepository attendanceRepo;


    // ✅ USER: Mark attendance
    public Attendance markAttendance(Long employeeId) {

        LocalDate today = LocalDate.now();

        Attendance attendance =
            attendanceRepo
            .findByEmployeeIdAndDate(employeeId, today)
            .orElseGet(() -> {
                Attendance a = new Attendance();
                a.setEmployeeId(employeeId);
                a.setDate(today);
                a.setStatus("PRESENT");
                return a;
            });

        if(attendance.getCheckIn()==null){
            attendance.setCheckIn(LocalTime.now());
        }
        else if(attendance.getCheckOut()==null){
            attendance.setCheckOut(LocalTime.now());
            recalculate(attendance);
        }
        else{
            throw new RuntimeException("Already completed attendance today");
        }

        return attendanceRepo.save(attendance);
    }



    public void markAbsentUsersForToday() {

        LocalDate today = LocalDate.now();

        // 1️⃣ Aaj jin users ne attendance mark ki
        Set<Long> markedUserIds =
                attendanceRepository.findUserIdsMarkedToday(today)
                .stream()
                .collect(Collectors.toSet());

        // 2️⃣ All users lao
        List<User> allUsers = userRepository.findByActiveTrue();

        // 3️⃣ Jinhone mark nahi kiya → ABSENT
        for (User user : allUsers) {

            if (!markedUserIds.contains(user.getId())) {

                Attendance attendance = new Attendance();
                attendance.setEmployeeId(user.getId()); 
                attendance.setDate(today);
                attendance.setStatus("ABSENT");

                attendanceRepository.save(attendance);
            }
        }
    }

    private Attendance today(Long empId){

        LocalDate today=LocalDate.now();

        return attendanceRepo
            .findByEmployeeIdAndDate(empId,today)
            .orElseGet(()->{
                Attendance a=new Attendance();
                a.setEmployeeId(empId);
                a.setDate(today);
                a.setStatus("PRESENT");
                return a;
            });
    }

    // TODAY STATUS
    public Map<String,Object> todayStatus(Long empId){

        Attendance a=
            attendanceRepo
            .findByEmployeeIdAndDate(empId,LocalDate.now())
            .orElse(null);

        if(a==null){
            return Map.of(
                "checkIn",null,
                "checkOut",null,
                "workingHours","00:00"
            );
        }

        String work="00:00";

        if(a.getWorkingMinutes()!=null){
            int h=a.getWorkingMinutes()/60;
            int m=a.getWorkingMinutes()%60;
            work=h+":"+String.format("%02d",m);
        }

        return Map.of(
            "checkIn",a.getCheckIn(),
            "checkOut",a.getCheckOut(),
            "workingHours",work
        );
    }

    // DASHBOARD STATS
    public Map<String,Object> stats(Long empId){

        long present =
            attendanceRepo.countByEmployeeIdAndStatus(empId,"PRESENT");

        long absent =
            attendanceRepo.countByEmployeeIdAndStatus(empId,"ABSENT");

        double rate =
            (present+absent)==0?0:
            (present*100.0)/(present+absent);

        return Map.of(
            "attendanceRate",rate,
            "presentDays",present,
            "absentDays",absent
        );
    }


    // ✅ USER: Attendance history
    public List<Attendance> history(Long empId){
        return attendanceRepo.findByEmployeeId(empId);
    }

    // ✅ USER: Check today status
    public boolean isAttendanceMarkedToday(long userId) {
        return attendanceRepository.existsByUserIdAndDate(userId, LocalDate.now());
    }

    // ✅ ADMIN: All attendance
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // ✅ ADMIN: User-wise attendance
    public List<Attendance> getAttendanceByUser(long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    // ✅ ADMIN: Date-wise attendance
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    // ✅ ADMIN: Today summary
    public Map<String, Long> todaySummary() {
        long present = attendanceRepository.countByStatus("PRESENT");
        return Map.of(
            "present", present
        );
    }

     // USER CHECK-IN
    public Attendance checkIn(Long empId){

        LocalTime now=LocalTime.now();

        if(now.isBefore(LocalTime.of(9,0))){
            throw new RuntimeException("Check-in allowed after 9 AM");
        }

        Attendance a=today(empId);

        if(a.getCheckIn()!=null){
            throw new RuntimeException("Already checked in");
        }

        a.setCheckIn(now);
        a.setStatus("PRESENT");

        return attendanceRepo.save(a);
    }

    // USER CHECK-OUT

    public Attendance checkOut(Long empId){

        Attendance a=today(empId);

        if(a.getCheckIn()==null){
            throw new RuntimeException("Check-in first");
        }

        if(a.getCheckOut()!=null){
            throw new RuntimeException("Already checked out");
        }

        a.setCheckOut(LocalTime.now());

        int minutes=(int)Duration
            .between(a.getCheckIn(),a.getCheckOut())
            .toMinutes();

        a.setWorkingMinutes(minutes);

        return attendanceRepo.save(a);
    }


    // ✅ ADMIN: Attendance list
    public List<Attendance> getAttendance(
            Long employeeId,
            LocalDate from,
            LocalDate to,
            String status
    ) {

        if (status == null || status.isBlank()) {
            return attendanceRepository
                    .findByEmployeeIdAndDateBetween(employeeId, from, to);
        }

        return attendanceRepository
                .findByEmployeeIdAndDateBetweenAndStatus(
                        employeeId, from, to, status
                );
    }

    // ✅ ADMIN: Attendance stats (top cards)
    public Map<String, Object> getStats(Long employeeId) {

        List<Attendance> list =
                attendanceRepo.findByEmployeeId(employeeId);

        int present = 0;
        int absent = 0;
        long totalMinutes = 0;

        LocalTime todayCheckIn = null;
        LocalTime todayCheckOut = null;

        LocalDate today = LocalDate.now();

        for (Attendance a : list) {

            if ("PRESENT".equals(a.getStatus())) {
                present++;

                if (a.getCheckIn() != null && a.getCheckOut() != null) {
                    totalMinutes += Duration
                            .between(a.getCheckIn(), a.getCheckOut())
                            .toMinutes();
                }
            }

            if ("ABSENT".equals(a.getStatus())) {
                absent++;
            }

            // today check-in / out
            if (today.equals(a.getDate())) {
                todayCheckIn = a.getCheckIn();
                todayCheckOut = a.getCheckOut();
            }
        }

        double avgHours =
                present == 0 ? 0 : (totalMinutes / 60.0) / present;

        Map<String, Object> map = new HashMap<>();
        map.put("averageHours", String.format("%.2f", avgHours));
        map.put("todayCheckIn", todayCheckIn);
        map.put("todayCheckOut", todayCheckOut);
        map.put("presentDays", present);
        map.put("absentDays", absent);

        return map;
    }

    public void recalculate(Attendance a) {

        if (a.getCheckIn() != null && a.getCheckOut() != null) {

            int totalMinutes =
                (int) Duration.between(a.getCheckIn(), a.getCheckOut()).toMinutes();

            int breakMin =
                java.util.Objects.requireNonNullElse(a.getBreakMinutes(), 0);

            a.setWorkingMinutes(Math.max(0, totalMinutes - breakMin));
        }
    }


}

