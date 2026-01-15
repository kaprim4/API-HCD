package com.example.HCOData.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotPAErrorException extends RuntimeException {
    public NotPAErrorException(String message) {
        super(message);
    }
}
