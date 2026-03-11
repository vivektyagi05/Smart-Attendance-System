package com.smart.attendance.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.model.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.security.JwtUtil;
import com.smart.attendance.service.AttendanceService;
import com.smart.attendance.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceService attendanceService;



    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.createUser(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        User authenticatedUser = userService.loginUser(
                user.getEmail(),
                user.getPassword()
        );

        if (authenticatedUser == null) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("message", "Invalid Email or Password"));
        }

        String token = jwtUtil.generateToken(
                authenticatedUser.getEmail(),
                authenticatedUser.getRole()
        );

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "role", authenticatedUser.getRole(),
                        "email", authenticatedUser.getEmail(),
                        "userId", authenticatedUser.getId()  
                      )
                );


    }
    @GetMapping("/dashboard")
    public Map<String,Object> dashboard(
        @AuthenticationPrincipal String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Map<String,Object> stats =
            attendanceService.stats(user.getId());

        return Map.of(
            "name",user.getName(),
            "attendanceRate",stats.get("attendanceRate"),
            "presentDays",stats.get("presentDays"),
            "absentDays",stats.get("absentDays"),
            "productivityScore",88,   // later productivity table
            "pendingTasks",8,
            "leaveBalance",12,
            "salaryStatus","Pending"
        );
    }

}
