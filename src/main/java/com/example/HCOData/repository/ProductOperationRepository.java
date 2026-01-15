package com.example.HCOData.repository;

import com.example.HCOData.model.ProductOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOperationRepository extends JpaRepository<ProductOperation,Long> {


    @Query("SELECT p from ProductOperation p  join p.brandOperation b join b.sousCategory sc join sc.category c join c.operations o where o.uid =:idOperation ")
    List<ProductOperation> findAllProductOperation(@Param("idOperation") String idOperation);



    @Query("SELECT p from ProductOperation p")
    List<ProductOperation> findAllProductOperation();
}
