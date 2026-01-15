package com.example.HCOData.repository;

import com.example.HCOData.model.ResponseAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageResponseAiRepository extends JpaRepository<ResponseAI,Long> {
    ResponseAI findByUid(String uid);
}
