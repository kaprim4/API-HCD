package com.example.HCOData.repository;

import com.example.HCOData.model.ModerationFlags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModerationFlagJPARepository extends JpaRepository<ModerationFlags,Long> {

	@Query("SELECT m FROM ModerationFlags m WHERE m.flag = :flag")
    Optional<ModerationFlags> findByFlag(@Param("flag") String flag);
	
	
	
}
