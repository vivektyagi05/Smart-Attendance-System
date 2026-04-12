package com.smart.attendance.service;

import com.smart.attendance.entity.Document;
import com.smart.attendance.entity.User;
import com.smart.attendance.repository.DocumentRepository;
import com.smart.attendance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repo;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ActivityLogService logService;
    
    // 🔥 Upload Document
    public void upload(MultipartFile file, String type, String email) {

        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF allowed ❌");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try {
            Path path = Paths.get("src/main/resources/static/uploads/docs/" + fileName);

            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            // 🔥 Check existing document
            Optional<Document> existing = repo.findByUserIdAndType(user.getId(), type);

            Document doc;

            if (existing.isPresent()) {
                doc = existing.get();
                doc.setFileName(fileName); // replace
            } else {
                doc = new Document();
                doc.setUser(user);
                doc.setType(type);
                doc.setFileName(fileName);
            }

            doc.setUploadTime(LocalDateTime.now());

            repo.save(doc);
            logService.log(email, "Uploaded " + type);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed");
        }
    }

    // 🔥 Get Documents
    public List<Document> getDocs(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repo.findByUserId(user.getId());
    }

    // 🔥 Delete Document
    public void delete(String type, String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Document doc = repo.findByUserIdAndType(user.getId(), type)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        repo.delete(doc);
        logService.log(email, "Deleted " + type);
    }
}