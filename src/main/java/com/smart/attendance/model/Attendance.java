package com.smart.attendance.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(
    name="attendance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id","date"})
    }
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;


    private LocalTime checkIn;
    private LocalTime checkOut;

    private String status; // PRESENT / ABSENT / LEAVE

    private Integer workingMinutes;
    private Integer breakMinutes;

    

    

    // ===== Getters & Setters =====



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }
    public Integer getWorkingMinutes() {
        return workingMinutes;
    }
    public void setWorkingMinutes(Integer workingMinutes) {
        this.workingMinutes = workingMinutes;
    }
    public Integer getBreakMinutes() {
        return breakMinutes;
    }
    public void setBreakMinutes(Integer breakMinutes) {
        this.breakMinutes = breakMinutes;
    }

    

}
