package com.example.HCOData.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.HCOData.model.OperationTerms;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTermsRepository extends JpaRepository<OperationTerms, String> {

	//List<OperationTerms> findByOperation_uid(String idOperation);

	@Query("SELECT ot FROM OperationTerms ot JOIN FETCH ot.operation WHERE ot.operation.uid = :uid")
	List<OperationTerms> findByOperation_uid(@Param("uid") String uid);

}
