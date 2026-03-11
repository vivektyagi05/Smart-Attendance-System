// package com.smart.attendance.model;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "chat_messages")
// public class ChatMessage {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private Long roomId;
//     private Long senderId;
//     private Long receiverId;

//     private String message;

//     private boolean readStatus = false;

//     private LocalDateTime createdAt =
//             LocalDateTime.now();

//     // ===== getters setters =====
//     public Long getId(){ return id; }

//     public Long getRoomId(){ return roomId; }
//     public void setRoomId(Long r){ this.roomId = r; }

//     public Long getSenderId(){ return senderId; }
//     public void setSenderId(Long s){ this.senderId = s; }

//     public Long getReceiverId(){ return receiverId; }
//     public void setReceiverId(Long r){ this.receiverId = r; }

//     public String getMessage(){ return message; }
//     public void setMessage(String m){ this.message = m; }

//     public boolean isReadStatus(){ return readStatus; }
//     public void setReadStatus(boolean r){ this.readStatus = r; }

//     public LocalDateTime getCreatedAt(){ return createdAt; }
// }
