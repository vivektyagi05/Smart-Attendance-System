package com.smart.attendance.service;

import com.smart.attendance.entity.User;
import com.smart.attendance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

        public void changePassword(String email, String currentPassword, String newPassword) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("INPUT current: " + currentPassword);
        System.out.println("DB password: " + user.getPassword());

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Wrong current password ❌");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        repo.save(user);

        System.out.println("Password updated ✅");
    }
}