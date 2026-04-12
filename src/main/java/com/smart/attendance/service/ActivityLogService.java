package com.smart.attendance.service;

import com.smart.attendance.entity.ActivityLog;
import com.smart.attendance.entity.User;
import com.smart.attendance.repository.ActivityLogRepository;
import com.smart.attendance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository repo;

    @Autowired
    private UserRepository userRepo;

    // 🔥 Save log
    public void log(String email, String action) {

        User user = userRepo.findByEmail(email).orElseThrow();

        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setAction(action);
        log.setTime(LocalDateTime.now());

        repo.save(log);
    }

    // 🔥 Get logs
    public List<ActivityLog> getLogs(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return repo.findByUserIdOrderByTimeDesc(user.getId());
    }
}