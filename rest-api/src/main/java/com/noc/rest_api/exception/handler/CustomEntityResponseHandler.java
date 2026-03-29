package com.noc.rest_api.exception.handler;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.noc.rest_api.exception.ExceptionResponse;
import com.noc.rest_api.exception.FileStorageException;
import com.noc.rest_api.exception.RequiredObjectIsNullException;

@ControllerAdvice
@RestController
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(exception = Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage(), 
            request.getDescription(false)
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = ResponseStatusException.class)
    public final ResponseEntity<ExceptionResponse> handleResponseException(ResponseStatusException ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
            ex.getStatusCode(),
            ex.getReason(), 
            request.getDescription(false)
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = RequiredObjectIsNullException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(ResponseStatusException ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
            ex.getStatusCode(),
            ex.getReason(), 
            request.getDescription(false)
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = FileNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleFileNotFoundException(ResponseStatusException ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
            ex.getStatusCode(),
            ex.getReason(), 
            request.getDescription(false)
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = FileStorageException.class)
    public final ResponseEntity<ExceptionResponse> handleFileStorageException(ResponseStatusException ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
            ex.getStatusCode(),
            ex.getReason(), 
            request.getDescription(false)
        );

        return ResponseEntity.status(response.status()).body(response);
    }
}
