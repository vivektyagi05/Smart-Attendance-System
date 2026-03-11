package com.smart.attendance.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String role;   // USER / ADMIN

    @Column(nullable = false)
    private boolean active = true;

    public boolean isActive() {
        return active;
    }



    private LocalDate createdAt;


    // ===== GETTERS & SETTERS =====

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }   

    public void setName(String name) {
        this.name = name;
    }   

    public String getEmail() {
        return email;
    }   

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }   

    public void setPassword(String password) {
        this.password = password;
    }   

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    

    public LocalDate getCreatedAt() {
        return createdAt;
    }

}
