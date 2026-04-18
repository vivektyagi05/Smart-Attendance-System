package com.smart.attendance.controller;

import com.smart.attendance.entity.Leave;
import com.smart.attendance.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/leave")
@CrossOrigin("*")
public class LeaveController {

    @Autowired
    private LeaveService service;


    @GetMapping("/summary")
    public Map<String, Integer> getSummary() {
        return service.getSummary();
    }
    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(
            @RequestParam String leaveType,
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam String reason,
            @RequestParam int days,
            @RequestParam(required = false) MultipartFile file
    ) {

        try {

            Leave leave = new Leave();
            leave.setLeaveType(leaveType);
            leave.setFromDate(LocalDate.parse(fromDate));
            leave.setToDate(LocalDate.parse(toDate));
            leave.setReason(reason);
            leave.setDays(days);
            leave.setStatus("PENDING");
            leave.setAppliedDate(LocalDate.now());

            // File Save
            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path path = Paths.get("uploads/" + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                leave.setFileName(fileName);
            }

            service.saveLeave(leave);

            return ResponseEntity.ok("Saved");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error");
        }
    }
    @GetMapping("/history")
    public List<Leave> getLeaveHistory(
            @RequestParam(required = false) String status
    ) {
        return service.getLeaveHistory(status);
    }
    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) throws IOException {

        Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {

        Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}