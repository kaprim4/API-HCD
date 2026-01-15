package com.example.HCOData.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HCOData.model.NewCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCategoryRepository extends JpaRepository<NewCategory, String> {

}
