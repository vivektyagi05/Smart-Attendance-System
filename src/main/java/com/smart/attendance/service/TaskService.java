package com.smart.attendance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Task;
import com.smart.attendance.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepo;

    // TODAY TASKS
    public List<Task> today(Long empId){
        return taskRepo.findByEmployeeIdAndDueDate(
                empId,LocalDate.now());
    }

    // ALL TASKS
    public List<Task> all(Long empId){
        return taskRepo.findByEmployeeId(empId);
    }

    // UPDATE STATUS
    public Task updateStatus(Long taskId,String status){

        Task t=taskRepo.findById(taskId).orElseThrow();

        t.setStatus(status);

        return taskRepo.save(t);
    }

    // DASHBOARD COUNT
    public Map<String,Object> stats(Long empId){

        long pending =
            taskRepo.countByEmployeeIdAndStatus(empId,"TODO");

        long done =
            taskRepo.countByEmployeeIdAndStatus(empId,"DONE");

        return Map.of(
            "pendingTasks",pending,
            "completedTasks",done
        );
    }
}
