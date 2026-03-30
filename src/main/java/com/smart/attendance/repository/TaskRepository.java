package com.smart.attendance.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.attendance.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTop2ByUserIdOrderByDueDateAsc(Long userId);
}