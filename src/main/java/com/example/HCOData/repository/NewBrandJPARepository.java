package com.example.HCOData.repository;

import com.example.HCOData.model.NewBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewBrandJPARepository extends JpaRepository<NewBrand,Long> {

    @Query("SELECT br from NewBrand br where br.Brand_uid = :brandUid")
    Optional<NewBrand> findByBrandUid(String brandUid);
    
    @Query("SELECT br from NewBrand br where br.BrandUidLiaison = :brandUid")
    Optional<NewBrand> findNewBrand(String brandUid);
  

}
