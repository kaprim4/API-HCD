package com.example.HCOData.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class OperationNotFoundException extends RuntimeException {
    public OperationNotFoundException(String message) {
        super(message);
    }

}
