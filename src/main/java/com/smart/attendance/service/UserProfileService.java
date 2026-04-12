package com.smart.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.smart.attendance.dto.UserProfileDTO;
import com.smart.attendance.entity.UserProfile;
import com.smart.attendance.repository.UserProfileRepository;
import com.smart.attendance.repository.UserRepository;
import com.smart.attendance.entity.User;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDate;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository repo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityLogService logService;

    /**
     * Fetch profile and convert to DTO
     */
    public UserProfileDTO getProfileByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = repo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
                

        return convertToDTO(profile);
    }

    /**
     * Update profile and return updated DTO
     * @Transactional ensures the database changes are committed safely.
     */
    @Transactional
    public UserProfileDTO updateProfileByEmail(String email, UserProfileDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = repo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setFullName(dto.getFullName());
        profile.setPhone(dto.getPhone());
        profile.setDepartment(dto.getDepartment());
        profile.setDesignation(dto.getDesignation());
        profile.setJoiningDate(dto.getJoiningDate());
        profile.setEmployeeId(dto.getEmployeeId());
        profile.setStatus(dto.getStatus());


        repo.save(profile);
        logService.log(email, "Updated Profile");

        return convertToDTO(profile);
    }

    /**
     * Helper method to avoid repeating mapping code (Don't Repeat Yourself - DRY)
     */
    private UserProfileDTO convertToDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setFullName(profile.getFullName());
        dto.setPhone(profile.getPhone());
        dto.setDepartment(profile.getDepartment());
        dto.setDesignation(profile.getDesignation());
        dto.setJoiningDate(profile.getJoiningDate());
        dto.setEmployeeId(profile.getEmployeeId());
        dto.setStatus(profile.getStatus());
        dto.setProfileImage(profile.getProfileImage());

        
        // Handling the nested User entity safely
        if (profile.getUser() != null) {
            dto.setEmail(profile.getUser().getEmail());
        }

        
        return dto;
    }
    

    public String saveProfileImage(String email, MultipartFile file) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = repo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // 🔥 UNIQUE FILE NAME
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try {
            Path path = Paths.get("src/main/resources/static/uploads/profile/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            // 🔥 IMPORTANT: DB me save karo
            profile.setProfileImage(fileName);
            repo.save(profile);
            logService.log(email, "Updated Profile Photo");

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
        }
    }
}