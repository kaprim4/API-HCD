package com.example.HCOData.repository;

import com.example.HCOData.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product,Long> {

    @Query("SELECT SUM(p.unitPrice) FROM Product p WHERE p.id IN :ids")
    BigDecimal sumPricesByIds(@Param("ids") List<Long> ids);

}
