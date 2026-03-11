package com.smart.attendance.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smart.attendance.model.LeaveRequest;
import com.smart.attendance.service.LeaveService;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/apply")
    public LeaveRequest apply(
            @RequestParam String employeeCode,
            @RequestParam String leaveType,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String reason,
            @RequestParam MultipartFile pdf
    ) throws Exception {

        String uploadDir = "src/main/resources/static/uploads/";
        String fileName = System.currentTimeMillis()+"_"+pdf.getOriginalFilename();

        Path path = Path.of(uploadDir + fileName);
        Files.write(path, pdf.getBytes());

        LeaveRequest l = new LeaveRequest();
        l.setEmployeeCode(employeeCode);
        l.setLeaveType(leaveType);
        l.setFromDate(LocalDate.parse(from));
        l.setToDate(LocalDate.parse(to));
        l.setReason(reason);
        l.setPdfPath("/uploads/"+fileName);

        return leaveService.save(l);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<String,Long> stats(){

        long pending = leaveService.countByStatus("PENDING");
        long approved = leaveService.countByStatus("APPROVED");
        long rejected = leaveService.countByStatus("REJECTED");

        return Map.of(
            "pending",pending,
            "approved",approved,
            "rejected",rejected,
            "total",pending+approved+rejected
        );
    }

}
