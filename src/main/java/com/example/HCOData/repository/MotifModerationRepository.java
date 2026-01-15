package com.example.HCOData.repository;

import com.example.HCOData.model.MotifModeration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotifModerationRepository extends JpaRepository<MotifModeration, Long> {

}
