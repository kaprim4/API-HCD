package com.example.HCOData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HCOData.model.OperationBrandCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationBrandCategoryRepository extends JpaRepository<OperationBrandCategory, String> {
	
	List<OperationBrandCategory> findByOperation_uid(String idOperation);

}
