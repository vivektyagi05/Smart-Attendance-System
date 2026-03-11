package com.smart.attendance.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.model.Attendance;
import com.smart.attendance.model.Employee;
import com.smart.attendance.model.LeaveRequest;
import com.smart.attendance.repository.AttendanceRepository;
import com.smart.attendance.repository.EmployeeRepository;
import com.smart.attendance.service.AttendanceService;
import com.smart.attendance.service.LeaveService;
import com.smart.attendance.service.PdfService;
@RestController
@RequestMapping("/api/admin/employees")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminAttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private LeaveService leaveService;


    // 🔹 All attendance
    @GetMapping("/all")
    public List<Attendance> allAttendance() {
        return attendanceService.getAllAttendance();
    }

    // 🔹 User-wise
    @GetMapping("/user/{userId}")
    public List<Attendance> userAttendance(@PathVariable Long userId) {
        return attendanceService.getAttendanceByUser(userId);
    }

    // 🔹 Date-wise
    @GetMapping("/date")
    public List<Attendance> dateAttendance(
            @RequestParam String date) {

        return attendanceService.getAttendanceByDate(
                LocalDate.parse(date));
    }

    // 🔹 Today summary
    @GetMapping("/today-summary")
    public Map<String, Long> todaySummary() {
        return attendanceService.todaySummary();
    }
    // ✅ ADMIN: employee attendance table
    @GetMapping("/{code}/attendance")
    public List<Attendance> getAttendance(
            @PathVariable String code,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String status
    ) {

        Employee emp = employeeRepo
                .findByEmployeeCode(code)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate fromDate =
                from == null ? LocalDate.now().minusMonths(1) : LocalDate.parse(from);
        LocalDate toDate =
                to == null ? LocalDate.now() : LocalDate.parse(to);

        return attendanceService.getAttendance(
                emp.getId(),
                fromDate,
                toDate,
                status
        );
    }

    // ✅ ADMIN: stats
    @GetMapping("/{code}/stats")
    public Map<String, Object> stats(@PathVariable String code) {

        Employee emp = employeeRepo
                .findByEmployeeCode(code)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return attendanceService.getStats(emp.getId());
    }

    // ✏️ ADMIN EDIT ATTENDANCE
    @PutMapping("/{employeeCode}/{date}")
    public Map<String, String> editAttendance(
            @PathVariable String employeeCode,
            @PathVariable LocalDate date,
            @RequestBody Map<String, String> body
    ) {
        Long empId = employeeRepo.findIdByEmployeeCode(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance a = attendanceRepo
                .findByEmployeeIdAndDate(empId, date)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        if (body.containsKey("status")) {
            a.setStatus(body.get("status"));
        }

        if (body.containsKey("checkIn")) {
            a.setCheckIn(LocalTime.parse(body.get("checkIn")));
        }

        if (body.containsKey("checkOut")) {
            a.setCheckOut(LocalTime.parse(body.get("checkOut")));
        }

        String status = body.get("status");
        a.setStatus(status);

        if ("LEAVE".equals(status) || "ABSENT".equals(status)) {
            a.setCheckIn(null);
            a.setCheckOut(null);
            a.setWorkingMinutes(0);
            a.setBreakMinutes(0);
        } else {
            a.setCheckIn(LocalTime.parse(body.get("checkIn")));
            a.setCheckOut(LocalTime.parse(body.get("checkOut")));
            attendanceService.recalculate(a);
        }

        attendanceService.recalculate(a);
        attendanceRepo.save(a);

        return Map.of("message", "Attendance updated");
    }

    // ✅ ADMIN APPROVE LEAVE
        @PostMapping("/leave/approve")
        public Map<String, String> approveLeave(
                @RequestParam String employeeCode,
                @RequestParam String from,
                @RequestParam String to
        ) {
            Long empId = employeeRepo.findIdByEmployeeCode(employeeCode)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            LocalDate start = LocalDate.parse(from);
            LocalDate end = LocalDate.parse(to);

            while (!start.isAfter(end)) {
                Attendance a = attendanceRepo
                        .findByEmployeeIdAndDate(empId, start)
                        .orElse(new Attendance());

                a.setEmployeeId(empId);
                a.setDate(start);
                a.setStatus("LEAVE");
                a.setCheckIn(null);
                a.setCheckOut(null);
                a.setWorkingMinutes(0);
                a.setBreakMinutes(0);

                attendanceRepo.save(a);
                start = start.plusDays(1);
            }

            return Map.of("message", "Leave approved");
        }

        @GetMapping("/export")
        public ResponseEntity<byte[]> exportPdf(
                @RequestParam String employeeCode,
                @RequestParam String from,
                @RequestParam String to
        ) {
            Long empId = employeeRepo.findIdByEmployeeCode(employeeCode).get();

            List<Attendance> list =
                attendanceRepo.findByEmployeeIdAndDateBetween(
                    empId,
                    LocalDate.parse(from),
                    LocalDate.parse(to)
                );

            byte[] pdf = pdfService.generateAttendancePdf(list, employeeCode);

            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=attendance.pdf")
                .body(pdf);
        }

        // ✅ ADMIN:  leaves list

        @GetMapping("/leave/pending")
        public List<LeaveRequest> pendingLeaves(){
            return leaveService.getPending();
        }

        @PostMapping("/leave/{id}/approve")
        public String approve(@PathVariable Long id){

            LeaveRequest l = leaveService.get(id);
            l.setStatus("APPROVED");
            leaveService.save(l);

            // 🔥 auto attendance update
            approveLeave(l.getEmployeeCode(),
                l.getFromDate().toString(),
                l.getToDate().toString());

            return "Approved";
        }

        @PostMapping("/leave/{id}/reject")
        public String reject(@PathVariable Long id){

            LeaveRequest l = leaveService.get(id);
            l.setStatus("REJECTED");
            leaveService.save(l);

            return "Rejected";
        }



}
