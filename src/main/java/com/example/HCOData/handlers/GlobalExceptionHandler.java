package com.example.HCOData.handlers;

import com.example.HCOData.exception.InternalServerErrorException;
import com.example.HCOData.exception.NotPAErrorException;
import com.example.HCOData.exception.OperationNotFoundException;
import com.example.HCOData.exception.response.ErrorResponseNotPA;
import com.example.HCOData.exception.response.ErrorResponseOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.HCOData.exception.response.ErrorResponse;
import com.example.HCOData.exception.ImageNotFoundException;
import com.example.HCOData.exception.ImageBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleImageNotFoundException(ImageNotFoundException ex) {
        ErrorResponse res = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND);
        log.error("Image not found, status : {}, error : {}",  res.getStatus().value(), ex.getMessage());
       ex.printStackTrace();
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<ErrorResponseOperation> handleOperationNotFoundException(OperationNotFoundException ex) {

        ErrorResponseOperation res = new ErrorResponseOperation(ex.getMessage(), "REFOP_NO_EXIST");
        log.error("Operation not found, status : {}, error : {}", ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(ImageBadRequestException ex) {
        ErrorResponse res = new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST);
        log.error("Bad request Exception, status : {}, error : {}",  res.getStatus().value(), res.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotPAErrorException.class)
    public ResponseEntity<ErrorResponseNotPA> handleNotPABadRequestException(NotPAErrorException ex) {
        ErrorResponseNotPA res = new ErrorResponseNotPA(ex.getMessage());
        log.error("Bad request Exception, status : {}", res.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerErrorException ex, WebRequest request) {
        ErrorResponse res = new ErrorResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Exception internal server, status : {}, error : {}", res.getStatus().value(), res.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Global Exception internal server, status : {}, error : {} ", errorResponse.getStatus().value(), errorResponse.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
