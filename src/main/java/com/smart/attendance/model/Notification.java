package com.smart.attendance.model;
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name="notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;
    private String title;
    private String message;
    private String type;
    private boolean readStatus=false;
    private LocalDateTime createdAt=
        LocalDateTime.now();

    // getters setters
    public Long getId(){return id;}
    public Long getEmployeeId(){return employeeId;}
    public void setEmployeeId(Long e){this.employeeId=e;}
    public String getTitle(){return title;}
    public void setTitle(String t){this.title=t;}
    public String getMessage(){return message;}
    public void setMessage(String m){this.message=m;}
    public String getType(){return type;}
    public void setType(String t){this.type=t;}
    public boolean isReadStatus(){return readStatus;}
    public void setReadStatus(boolean s){this.readStatus=s;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime c){this.createdAt=c;}
    

}
