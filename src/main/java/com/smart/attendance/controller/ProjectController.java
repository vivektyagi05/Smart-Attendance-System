package com.smart.attendance.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smart.attendance.model.Project;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.ProjectService;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired private ProjectService projectService;
    @Autowired private UserRepository userRepository;

    @GetMapping("/my")
    public List<Project> myProjects(
            @AuthenticationPrincipal String email){

        User user=userRepository.findByEmail(email).orElseThrow();

        return projectService.myProjects(user.getId());
    }
}
