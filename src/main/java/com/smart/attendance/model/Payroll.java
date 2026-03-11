package com.smart.attendance.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private String month;
    private double netSalary;
    private String status; // PAID / PENDING

    // getters setters
    public Long getId(){return id;}
    public Long getEmployeeId(){return employeeId;}
    public void setEmployeeId(Long e){this.employeeId=e;}
    public String getMonth(){return month;}
    public void setMonth(String m){this.month=m;}
    public double getNetSalary(){return netSalary;}
    public void setNetSalary(double s){this.netSalary=s;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
    
}
