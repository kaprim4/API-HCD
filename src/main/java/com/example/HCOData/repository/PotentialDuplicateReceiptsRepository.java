package com.example.HCOData.repository;

import com.example.HCOData.model.PotentialDuplicateReceipts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PotentialDuplicateReceiptsRepository extends JpaRepository<PotentialDuplicateReceipts,Long> {

}
