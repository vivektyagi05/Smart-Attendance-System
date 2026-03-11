package com.smart.attendance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.attendance.dto.EmployeeDTO;
import com.smart.attendance.model.Employee;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.EmployeeRepository;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.util.PasswordUtil;


@Service
public class AdminEmployeeService {

    @Autowired private UserRepository userRepo;
    @Autowired private EmployeeRepository employeeRepo;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;

    // ✅ ADMIN: Create employee

    public Map<String, Object> createEmployee(EmployeeDTO dto) {

        // 1. generate password
        String rawPassword = PasswordUtil.generatePassword(8);

        // 2. create user
        User user = new User();
        user.setName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("USER");
        user.setActive(true);
        userRepo.save(user);

        // 3. create employee
        Employee emp = new Employee();
        emp.setUserId(user.getId());
        emp.setEmployeeCode("EMP" + (1000 + user.getId()));
        emp.setFullName(dto.getFullName());
        emp.setPhone(dto.getPhone());
        emp.setDepartment(dto.getDepartment());
        emp.setDesignation(dto.getDesignation());
        emp.setJoiningDate(dto.getJoiningDate());

        if (dto.getJoiningDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Joining date cannot be in the future");
        }

        if (!dto.getJoiningDate().isAfter(LocalDate.now())) {
            emp.setStatus("ACTIVE");
        } else {
            emp.setStatus("INACTIVE");
        }



        employeeRepo.save(emp);

        // 4. send email
        emailService.sendCredentials(user.getEmail(), rawPassword);

        return Map.of(
                "message", "Employee created",
                "employeeCode", emp.getEmployeeCode(),
                "email", user.getEmail()
        );
    }

    // ✅ ADMIN: Get all employees

    public List<Employee> getAllEmployees() {

        List<Employee> list = employeeRepo.findAll();

        for (Employee e : list) {
            userRepo.findById(e.getUserId())
                .ifPresent(u -> e.setEmail(u.getEmail()));
        }

        return list;
    }

    // ✅ ADMIN: Get employee by code

    public Employee getEmployeeByCode(String code) {
        return employeeRepo.findByEmployeeCode(code)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    //  ✅ ADMIN: Update employee

    public Employee updateEmployee(String code, EmployeeDTO dto) {

        Employee emp = employeeRepo.findByEmployeeCode(code)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        emp.setFullName(dto.getFullName());
        emp.setPhone(dto.getPhone());
        emp.setDepartment(dto.getDepartment());
        emp.setDesignation(dto.getDesignation());
        emp.setJoiningDate(dto.getJoiningDate());

        // status re-evaluate
        if (!dto.getJoiningDate().isAfter(LocalDate.now())) {
            emp.setStatus("ACTIVE");
        } else {
            emp.setStatus("INACTIVE");
        }

        return employeeRepo.save(emp);
    }


}
