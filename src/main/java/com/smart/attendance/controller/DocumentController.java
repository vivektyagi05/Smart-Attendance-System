package com.smart.attendance.controller;

import com.smart.attendance.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService service;

    // 🔥 Upload
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            Authentication auth) {

        service.upload(file, type, auth.getName());

        return ResponseEntity.ok("Uploaded");
    }

    // 🔥 Get all documents
    @GetMapping
    public ResponseEntity<?> getDocs(Authentication auth) {
        return ResponseEntity.ok(service.getDocs(auth.getName()));
    }

    // 🔥 Delete document
    @DeleteMapping("/{type}")
    public ResponseEntity<?> delete(
            @PathVariable String type,
            Authentication auth) {

        service.delete(type, auth.getName());

        return ResponseEntity.ok("Deleted");
    }
}