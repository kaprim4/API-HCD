package com.example.HCOData.repository;
import com.example.HCOData.model.NewCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewCategoryJPARepository extends JpaRepository<NewCategory,Long> {

    @Query("select ca from NewCategory ca where ca.category_uid = :categoryUid")
    Optional<NewCategory> findByCategoryUid(String categoryUid);


}
