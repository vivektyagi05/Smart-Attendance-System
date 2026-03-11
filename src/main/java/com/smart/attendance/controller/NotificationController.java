package com.smart.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.smart.attendance.model.Notification;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

 @Autowired private NotificationService notificationService;
 @Autowired private UserRepository userRepository;

 // ===============================
 // DASHBOARD RECENT
 // ===============================
 @GetMapping("/recent")
 public List<Notification> recent(
        @AuthenticationPrincipal String email){

  User user=userRepository.findByEmail(email).orElseThrow();

  return notificationService.recent(user.getId());
 }

 // ===============================
 // ALL NOTIFICATIONS PAGE
 // ===============================
 @GetMapping("/all")
 public List<Notification> all(
        @AuthenticationPrincipal String email){

  User user=userRepository.findByEmail(email).orElseThrow();

  return notificationService.all(user.getId());
 }

 // ===============================
 // MARK AS READ
 // ===============================
 @PutMapping("/{id}/read")
 public void read(@PathVariable Long id){
  notificationService.markRead(id);
 }
}
