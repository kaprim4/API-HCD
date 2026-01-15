package com.example.HCOData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.HCOData.model.Flags;

@Repository
public interface FlagsRepository extends JpaRepository<Flags, Long> {
	
	boolean existsByImageUid(String imageUid);

}
