package com.smart.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smart.attendance.model.Project;

public interface ProjectRepository
        extends JpaRepository<Project,Long>{

    List<Project> findByEmployeeId(Long empId);
}
