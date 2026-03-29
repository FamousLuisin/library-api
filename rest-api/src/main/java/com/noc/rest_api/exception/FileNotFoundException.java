package com.noc.rest_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FileNotFoundException extends ResponseStatusException {
    
    public FileNotFoundException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR, "File not found");
    }

    public FileNotFoundException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public FileNotFoundException(String message, Throwable cause){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }
}
