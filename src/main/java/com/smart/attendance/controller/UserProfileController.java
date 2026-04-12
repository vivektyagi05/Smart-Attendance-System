package com.smart.attendance.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.smart.attendance.dto.UserProfileDTO;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.nio.file.Files;
import java.util.Map;
import com.smart.attendance.service.UserProfileService;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {

        String email = authentication.getName(); // ✅ FIX

        return ResponseEntity.ok(service.getProfileByEmail(email));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestBody UserProfileDTO dto,
            Authentication authentication) {

        String email = authentication.getName(); // ✅ FIX

        return ResponseEntity.ok(service.updateProfileByEmail(email, dto));
    }
    @PostMapping("/upload-photo")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {

        String email = auth.getName();

        String fileName = service.saveProfileImage(email, file);

        return ResponseEntity.ok(
            Map.of("imageUrl", "/uploads/profile/" + fileName)
        );
    }
    
}