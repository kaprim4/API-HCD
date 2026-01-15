package com.example.HCOData.service.document;

import com.example.HCOData.model.Operation;
import com.example.HCOData.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService{


    @Autowired
    private OperationRepository operationRepository;

    @Override
    @Transactional
    public Optional<Operation> getOptionalOperation(String operation) {
        return operationRepository.getOperationByUid(operation);
    }
}
