package com.smart.attendance.controller;

import com.smart.attendance.dto.ChangePasswordDTO;
import com.smart.attendance.entity.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.security.JwtUtil;
import com.smart.attendance.service.ActivityLogService;
import com.smart.attendance.service.EmailService;
import com.smart.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired
    private ActivityLogService logService;
    @Autowired private UserService userService;

     private Map<String, String> otpStorage = new HashMap<>(); // Temporary storage (Real project mein Redis use karein)

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email").trim();
        String password = request.get("password");

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
            logService.log(email, "Logged In");
        
        // Agar email nahi mila ya password galat hua
        return ResponseEntity.status(401).body(Map.of("message", "Invalid Email or Password"));
    }

   

    @Autowired 
    private EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email").trim();
        Optional<User> userOptional = userRepo.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // 1. Generate 6-digit OTP
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            
            // 2. Database mein save karein (Real tracking)
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            userRepo.save(user);

            // 3. Real SMTP Email Send
            try {
                emailService.sendOtpEmail(email, otp);
                return ResponseEntity.ok(Map.of("message", "OTP sent to your email successfully"));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(Map.of("message", "Error sending email"));
            }
        }
        return ResponseEntity.status(404).body(Map.of("message", "User not found"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email").trim();
        String enteredOtp = request.get("otp");
        String newPassword = request.get("newPassword");

        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check: OTP match hona chahiye aur expired nahi hona chahiye
            if (user.getOtp() != null && 
                user.getOtp().equals(enteredOtp) && 
                user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                
                // Success: Password Hash karke save karein
                user.setPassword(passwordEncoder.encode(newPassword));
                
                // Security: OTP ko clear kar dein taaki dobara use na ho sake
                user.setOtp(null);
                user.setOtpExpiry(null);
                userRepo.save(user);

                return ResponseEntity.ok(Map.of("message", "Password has been reset successfully!"));
            }
        }
        return ResponseEntity.status(400).body(Map.of("message", "Invalid OTP or OTP has expired."));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid OTP"));
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body(Map.of("message", "OTP expired"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDTO dto,
            Authentication auth) {

        userService.changePassword(
            auth.getName(),
            dto.getCurrentPassword(),
            dto.getNewPassword()
        );

        return ResponseEntity.ok("Password changed successfully");
    }
}