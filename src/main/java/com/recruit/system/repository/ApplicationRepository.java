package com.recruit.system.repository;

import com.recruit.system.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserId(Long userId);
    @Query("SELECT DISTINCT a FROM Application a LEFT JOIN FETCH a.documents")
    List<Application> findAllWithDocuments();
    List<Application> findByJobId(Long jobId);
    boolean existsByUserIdAndJobId(Long userId, Long jobId);

}