package com.example.HCOData.repository;


import com.example.HCOData.model.RechercheCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechercheCategoryJPARepository extends JpaRepository<RechercheCategory,Long> {

    @Query("SELECT rc from RechercheCategory rc order by rc.treatmentOrder ")
    List<RechercheCategory> findAllRechercheCategory();
}
