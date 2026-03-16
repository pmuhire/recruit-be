package com.recruit.system.repository;

import com.recruit.system.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByApplicationId(Long applicationId);

}