package com.smart.attendance.repository;

import com.smart.attendance.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByUserId(Long userId);

    Optional<Document> findByUserIdAndType(Long userId, String type);
}