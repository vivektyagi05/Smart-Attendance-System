package com.smart.attendance.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private String description;
    private String priority; // HIGH / MEDIUM / LOW

    private String title;
    private String status; // TODO / INPROGRESS / DONE
    private LocalDate dueDate;

    // getters setters
    public Long getId(){return id;}
    public Long getEmployeeId(){return employeeId;}
    public void setEmployeeId(Long e){this.employeeId=e;}
    public String getTitle(){return title;}
    public void setTitle(String t){this.title=t;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
    public LocalDate getDueDate(){return dueDate;}
    public void setDueDate(LocalDate d){this.dueDate=d;}
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
}
