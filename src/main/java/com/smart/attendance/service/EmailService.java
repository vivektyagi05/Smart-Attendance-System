package com.smart.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCredentials(String to, String password) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Welcome to Attendance System");
        msg.setText("""
                Your account has been created.

                Email: %s
                Temporary Password: %s

                Please login and change your password.
                """.formatted(to, password));

        mailSender.send(msg);
    }

}
