package com.noc.rest_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends ResponseStatusException {
    
    public FileStorageException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR, "File Storage error");
    }

    public FileStorageException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public FileStorageException(String message, Throwable cause){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }
}
