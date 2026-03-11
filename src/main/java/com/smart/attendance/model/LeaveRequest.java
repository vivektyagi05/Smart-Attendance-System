package com.smart.attendance.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeCode;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;

    private String pdfPath;


    private String status = "PENDING"; // PENDING / APPROVED / REJECTED

    // getters setters
    public Long getId(){return id;}
    public String getEmployeeCode(){return employeeCode;}
    public void setEmployeeCode(String e){this.employeeCode=e;}

    public String getLeaveType(){return leaveType;}
    public void setLeaveType(String l){this.leaveType=l;}

    public LocalDate getFromDate(){return fromDate;}
    public void setFromDate(LocalDate d){this.fromDate=d;}

    public LocalDate getToDate(){return toDate;}
    public void setToDate(LocalDate d){this.toDate=d;}

    public String getReason(){return reason;}
    public void setReason(String r){this.reason=r;}

    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}

    public String getPdfPath(){return pdfPath;}
    public void setPdfPath(String p){this.pdfPath=p;}
}
