package com.smart.attendance.controller;

import com.smart.attendance.dto.SalaryResponse;
import com.smart.attendance.entity.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.PdfService;
import com.smart.attendance.service.SalaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/payslip")
public class PayslipController {

    @Autowired private PdfService pdfService;
    @Autowired private SalaryService salaryService;
    @Autowired private UserRepository userRepo;


    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPayslip(Authentication auth) {

        String email = auth.getName();

        User user = userRepo.findByEmail(email).orElseThrow();

        SalaryResponse salary = salaryService.getSalary(user.getId());

        byte[] pdf = pdfService.generatePayslip(
                user.getFirstName(),
                user.getId(),
                salary
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payslip.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}