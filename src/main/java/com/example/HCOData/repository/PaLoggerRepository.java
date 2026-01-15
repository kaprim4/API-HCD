package com.example.HCOData.repository;


import com.example.HCOData.model.PaLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaLoggerRepository extends JpaRepository<PaLogger, UUID> {

    boolean existsByImageUid(String uid);
    PaLogger findByImageUid(String uid);
}
