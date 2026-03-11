package com.smart.attendance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.attendance.dto.EmployeeDTO;
import com.smart.attendance.model.Employee;
import com.smart.attendance.service.AdminEmployeeService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminEmployeeController {

    @Autowired
    private AdminEmployeeService service;

    // ✅ CREATE EMPLOYEE (THIS WAS MISSING)
    @PostMapping("/employees")
    public Map<String, Object> createEmployee(
            @RequestBody EmployeeDTO dto) {
        return service.createEmployee(dto);
    }

    // ✅ GET ALL EMPLOYEES
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    // ✅ GET SINGLE EMPLOYEE
    @GetMapping("/employees/{code}")
    public Employee getEmployee(@PathVariable String code) {
        return service.getEmployeeByCode(code);
    }

    @PutMapping("/employees/{code}")
    public Employee updateEmployee(
            @PathVariable String code,
            @RequestBody EmployeeDTO dto
    ) {
        return service.updateEmployee(code, dto);
    }

}
