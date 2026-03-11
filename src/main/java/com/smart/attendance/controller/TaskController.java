package com.smart.attendance.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.smart.attendance.model.Task;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.TaskService;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired private TaskService taskService;
    @Autowired private UserRepository userRepository;

    @GetMapping("/today")
    public List<Task> today(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        return taskService.today(user.getId());
    }

    @GetMapping("/all")
    public List<Task> all(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        return taskService.all(user.getId());
    }

    @PutMapping("/{id}/status")
    public Task update(
            @PathVariable Long id,
            @RequestParam String status){

        return taskService.updateStatus(id,status);
    }
}
