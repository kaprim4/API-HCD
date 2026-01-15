package com.example.HCOData.repository;

import com.example.HCOData.model.RechercheBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechercheBrandJPARepository extends JpaRepository<RechercheBrand,Long> {
    @Query("SELECT rb from RechercheBrand rb order by rb.treatmentOrder")
    List<RechercheBrand> findAllRechercheBrand();
}
