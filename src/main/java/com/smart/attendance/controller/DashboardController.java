package com.smart.attendance.controller;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smart.attendance.model.User;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.service.DashboardSummaryService;
import com.smart.attendance.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

 @Autowired private DashboardSummaryService summaryService;
 @Autowired private UserRepository userRepository;
 @Autowired private DashboardService dashboardService;

 @GetMapping("/summary")
 public Map<String,Object> summary(
        @AuthenticationPrincipal String email){

  User user =
      userRepository.findByEmail(email).orElseThrow();

  return summaryService.summary(user.getId());
 }

  @GetMapping("/data")
    public Map<String,Object> data(
            @AuthenticationPrincipal String email){

        User user =
            userRepository.findByEmail(email).orElseThrow();

        return dashboardService.dashboard(user.getId());
    }
}
