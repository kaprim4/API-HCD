package com.example.HCOData.service.document;

import com.example.HCOData.model.Operation;

import java.util.Optional;

public interface OperationService {

    Optional<Operation> getOptionalOperation(String operation);
}
