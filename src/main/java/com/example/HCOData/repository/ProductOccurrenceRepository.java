package com.example.HCOData.repository;

import com.example.HCOData.model.ProductOccurence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOccurrenceRepository extends JpaRepository<ProductOccurence,Long> {

    boolean existsByProductName(String productName);

    ProductOccurence findByProductName(String productName);
}
