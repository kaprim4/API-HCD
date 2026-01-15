package com.example.HCOData.repository;

import com.example.HCOData.model.Operation;
import com.example.HCOData.model.SignOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {

    Operation findByUid(String id);

    boolean existsByUid(String uid);

    @Query("SELECT p FROM Operation p where p.uid = :id")
    Optional<Operation> getOperationByUid(String id);

    @Query("SELECT so from SignOperation so where so.operation.uid =:idOperation")
    List<SignOperation> findAllSignOperation(String idOperation);

}
