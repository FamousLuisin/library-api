package com.noc.rest_api.exception;

import java.util.Date;

import org.springframework.http.HttpStatusCode;

public record ExceptionResponse(HttpStatusCode status, Integer code, String message, String path, Date timestamp)  {

    public ExceptionResponse(HttpStatusCode status, String message, String path){
        this(status, status.value(), message, path, new Date());
    }
}
