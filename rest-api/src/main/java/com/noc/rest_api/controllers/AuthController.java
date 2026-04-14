package com.noc.rest_api.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noc.rest_api.dto.security.AccountCredentialsDto;
import com.noc.rest_api.services.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/auth")
@Tag(name = "Authentication endpoint")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/singin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDto credentialsDto){
        
        if (credentialsIsInvalid(credentialsDto)) 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request! 1");

        var token = authService.signIn(credentialsDto);

        if (token == null) 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request! 2");

        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody AccountCredentialsDto credentialsDto){
        var account = authService.create(credentialsDto);

        return ResponseEntity.ok(account);
    }

    @PostMapping(path = "/refresh/{username}")
    public ResponseEntity<?> refreshToken(@PathVariable(name = "username") String username, @RequestHeader("Authorization") String refreshToken){
        if (parametersAreInvalid(username, refreshToken)) 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request! 1");

        var token = authService.refresh(username, refreshToken);

        if (token == null) 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request! 2");

        return ResponseEntity.ok(token);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(refreshToken) || StringUtils.isBlank(username);
    }

    private boolean credentialsIsInvalid(AccountCredentialsDto credentialsDto) {
        return credentialsDto == null || StringUtils.isBlank(credentialsDto.getPassword()) || StringUtils.isBlank(credentialsDto.getPassword());
    }
}
