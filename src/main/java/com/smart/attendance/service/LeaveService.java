package com.smart.attendance.service;

import com.smart.attendance.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.smart.attendance.entity.Leave;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository repo;

    public Map<String, Integer> getSummary() {

        int casual = repo.getTotalByType("Casual Leave");
        int sick = repo.getTotalByType("Sick Leave");
        int used = repo.getTotalByStatus("APPROVED");

        int totalBalance = 20 - used; // yearly limit = 20

        Map<String, Integer> data = new HashMap<>();
        data.put("totalBalance", totalBalance);
        data.put("casualLeave", casual);
        data.put("sickLeave", sick);
        data.put("usedThisYear", used);

        return data;
    }
    public void saveLeave(com.smart.attendance.entity.Leave leave) {
        repo.save(leave);
    }


    public List<Leave> getLeaveHistory(String status) {

        if (status != null) {
            return repo.findTop10ByStatusOrderByAppliedDateDesc(status);
        }

        return repo.findTop10ByOrderByAppliedDateDesc();
    }
}