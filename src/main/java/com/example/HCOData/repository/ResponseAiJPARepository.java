package com.example.HCOData.repository;

import com.example.HCOData.model.ResponseAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseAiJPARepository extends JpaRepository<ResponseAI,Long> {

    @Query("SELECT a  FROM ResponseAI a inner join a.operations o where o.uid = :idOperation")
    List<ResponseAI> findAllResponseAiByOperation(@Param("idOperation") String idOperation);
}
