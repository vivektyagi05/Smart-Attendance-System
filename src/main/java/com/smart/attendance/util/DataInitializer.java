package com.smart.attendance.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.smart.attendance.entity.User;
import com.smart.attendance.repository.UserRepository;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Admin User create ho raha hai
            User admin = new User();
            admin.setFirstName("Vivek");
            admin.setLastName("Admin");
            admin.setEmail("admin@test.com");
            admin.setRole("ADMIN");
            admin.setPassword(passwordEncoder.encode("admin123")); // Hash password
            userRepository.save(admin);

            // Employee User create ho raha hai
            User user = new User();
            user.setFirstName("Vivek");
            user.setLastName("Employee");
            user.setEmail("user@test.com");
            user.setRole("USER");
            user.setPassword(passwordEncoder.encode("user123")); // Hash password
            userRepository.save(user);
            
            System.out.println("✅ Default users created with BCrypt passwords!");
        }
    }
}