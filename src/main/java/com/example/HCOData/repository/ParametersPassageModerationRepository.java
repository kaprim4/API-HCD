package com.example.HCOData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HCOData.model.ParametresPassageToModeration;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametersPassageModerationRepository extends JpaRepository<ParametresPassageToModeration, Long> {
	
	ParametresPassageToModeration findByOperationUid(String uid);

}
