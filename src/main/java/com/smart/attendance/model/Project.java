package com.smart.attendance.model;

import jakarta.persistence.*;

@Entity
@Table(name="projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private String name;
    private String client;
    private int progress; // %

    private String status; // ONGOING / DONE

    // getters setters
    public Long getId(){return id;}
    public Long getEmployeeId(){return employeeId;}
    public void setEmployeeId(Long e){this.employeeId=e;}
    public String getName(){return name;}
    public void setName(String n){this.name=n;}
    public String getClient(){return client;}
    public void setClient(String c){this.client=c;}
    public int getProgress(){return progress;}
    public void setProgress(int p){this.progress=p;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
}
