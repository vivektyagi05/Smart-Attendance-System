package com.smart.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "events") // Reserved keyword se bachne ke liye
@Getter 
@Setter 
@NoArgsConstructor // Hibernate ke liye zaroori
@AllArgsConstructor // Testing ke liye
@Builder // Object banane mein aasani ke liye
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Title khali nahi hona chahiye
    private String title;

    @Column(nullable = false)
    private LocalDate eventDate;

    private String type; // e.g., "Holiday", "Meeting"

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
}