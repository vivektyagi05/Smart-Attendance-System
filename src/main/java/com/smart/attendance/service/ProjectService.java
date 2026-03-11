package com.smart.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.Project;
import com.smart.attendance.repository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    public List<Project> myProjects(Long empId){
        return projectRepo.findByEmployeeId(empId);
    }
}
