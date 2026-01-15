package com.example.HCOData.repository;

import com.example.HCOData.model.ProduitResponseAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsResponseAiRepository extends JpaRepository<ProduitResponseAI,Long> {


    @Query("SELECT p from ProduitResponseAI p inner join p.responseAI ra inner join ra.operations o where o.uid = :idOperation")
    List<ProduitResponseAI> findAllProductResponseAi(@Param("idOperation") String idOperation);
}
