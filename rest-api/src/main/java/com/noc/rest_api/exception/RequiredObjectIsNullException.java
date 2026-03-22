package com.noc.rest_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends ResponseStatusException {
    
    public RequiredObjectIsNullException(){
        super(HttpStatus.BAD_REQUEST, "It is not allowed to persist a null object");
    }

    public RequiredObjectIsNullException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
