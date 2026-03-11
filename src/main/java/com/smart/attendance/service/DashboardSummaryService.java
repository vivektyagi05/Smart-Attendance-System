package com.smart.attendance.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.repository.EmployeeRepository;
import com.smart.attendance.repository.LeaveRepository;
import com.smart.attendance.repository.PayrollRepository;
import com.smart.attendance.repository.TaskRepository;

@Service
public class DashboardSummaryService {

 @Autowired private PayrollRepository payrollRepo;
 @Autowired private LeaveRepository leaveRepo;
 @Autowired private TaskRepository taskRepo;
 @Autowired private EmployeeRepository employeeRepo;

 public Map<String,Object> summary(Long empId){

  String salaryStatus =
      payrollRepo.currentMonthStatus(empId);

  // 🔥 FIX HERE
  String empCode = employeeRepo.findById(empId)
          .map(e -> e.getEmployeeCode())
          .orElse(null);

  long leaveCount =
      leaveRepo.currentMonthLeaveCount(empCode);

  long pendingTasks =
      taskRepo.countByEmployeeIdAndStatus(
            empId,"TODO");

  return Map.of(
        "salaryStatus",
            salaryStatus==null?"Pending":salaryStatus,
        "leaveUsed",leaveCount,
        "pendingTasks",pendingTasks
  );
 }
}