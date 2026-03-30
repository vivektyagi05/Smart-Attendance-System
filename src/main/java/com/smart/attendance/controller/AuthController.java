package com.smart.attendance.controller;

import com.smart.attendance.entity.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
    String email = request.get("email").trim();
    String password = request.get("password").trim();

        // 1. User ko database se Optional ke taur par nikala
        Optional<User> userOptional = userRepo.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // 2. Password Match Check (Plain text vs Hashed)
            if (passwordEncoder.matches(password, user.getPassword())) {
                
                // 3. Token Generate
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
                
                // 4. Proper Response
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole(),
                    "email", user.getEmail(),
                    "userId", user.getId(),
                    "name", user.getFirstName()
                ));
            }
        }
        
        // Agar email nahi mila ya password galat hua
        return ResponseEntity.status(401).body(Map.of("message", "Invalid Email or Password"));
    }
}