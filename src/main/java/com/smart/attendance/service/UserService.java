package com.smart.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.User;
import com.smart.attendance.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String name, String email, String password, String role) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role.toUpperCase());
        user.setActive(true);

        return userRepository.save(user);
    }

    // 🔹 ADMIN: GET ALL USERS
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 🔹 ADMIN: GET USER BY ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 🔹 ADMIN: ACTIVATE / DEACTIVATE USER
    public User toggleUserStatus(Long userId, boolean active) {
        User user = getUserById(userId);
        user.setActive(active);
        return userRepository.save(user);
    }

    // ✅ LOGIN
    public User loginUser(String email, String rawPassword) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return null;

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return null;
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
            userRepository.save(user);
        }

        return user;
    }

}
