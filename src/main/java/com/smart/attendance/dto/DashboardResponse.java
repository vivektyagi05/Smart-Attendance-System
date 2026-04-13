package com.smart.attendance.dto;

import lombok.*;
import java.util.List;
import com.smart.attendance.entity.*;

@Data // Isme Getter, Setter, RequiredArgsConstructor sab shamil hai
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private String fullName;
    private String checkInTime;
    private double performanceScore;
    private List<Task> todayTasks;
    private List<Event> events;
    private double salary;
    private double bonus;
    private String nextPayment;
    private String createdAt;

    // Getters and Setters (Lombok @Data se generate ho jayenge)

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public List<Task> getTodayTasks() {
        return todayTasks;
    }

    public void setTodayTasks(List<Task> todayTasks) {
        this.todayTasks = todayTasks;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public String getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment(String nextPayment) {
        this.nextPayment = nextPayment;
    }

    public String getCreatedAt() {
        return createdAt;
    }   

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;

    }

    
}