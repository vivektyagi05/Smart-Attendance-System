package com.smart.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Notification;
import com.smart.attendance.repository.NotificationRepository;

@Service
public class NotificationService {

 @Autowired
 private NotificationRepository notificationRepo;

 // ===============================
 // CREATE NOTIFICATION (AUTO USE)
 // ===============================
 public Notification create(
        Long empId,
        String title,
        String message,
        String type){

  Notification n=new Notification();

  n.setEmployeeId(empId);
  n.setTitle(title);
  n.setMessage(message);
  n.setType(type);

  return notificationRepo.save(n);
 }

 // ===============================
 // DASHBOARD RECENT NOTIFICATIONS
 // ===============================
 public List<Notification> recent(Long empId){
  return notificationRepo
        .findTop5ByEmployeeIdOrderByCreatedAtDesc(empId);
 }

 // ===============================
 // ALL NOTIFICATIONS PAGE
 // ===============================
 public List<Notification> all(Long empId){
  return notificationRepo
        .findByEmployeeIdOrderByCreatedAtDesc(empId);
 }

 // ===============================
 // MARK READ
 // ===============================
 public void markRead(Long id){

  Notification n=
        notificationRepo.findById(id).orElseThrow();

  n.setReadStatus(true);

  notificationRepo.save(n);
 }
}
