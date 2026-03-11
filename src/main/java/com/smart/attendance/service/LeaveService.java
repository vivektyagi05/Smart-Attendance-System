package com.smart.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.model.LeaveRequest;
import com.smart.attendance.repository.LeaveRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository repo;

    public List<LeaveRequest> getPending(){
        return repo.findByStatus("PENDING");
    }

    public LeaveRequest get(Long id){
        return repo.findById(id).orElseThrow();
    }

    public LeaveRequest save(LeaveRequest l){
        return repo.save(l);
    }

    public long countByStatus(String status) {
        return repo.findByStatus(status).size();
    }

}
