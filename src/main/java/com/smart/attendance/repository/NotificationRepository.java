package com.smart.attendance.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smart.attendance.model.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification,Long>{

 List<Notification>
     findTop5ByEmployeeIdOrderByCreatedAtDesc(Long empId);

 List<Notification>
     findByEmployeeIdOrderByCreatedAtDesc(Long empId);
}
