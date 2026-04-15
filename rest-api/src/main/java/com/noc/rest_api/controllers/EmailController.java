package com.noc.rest_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.noc.rest_api.dto.request.EmailRequestDto;
import com.noc.rest_api.services.EmailService;

@RestController
@RequestMapping(path = "/api/email")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto emailRequestDto){
        emailService.sendSimpleEmail(emailRequestDto);

        return ResponseEntity.ok("Email sent with success!!");
    }

    @PostMapping("/attach")
    public ResponseEntity<String> sendEmailWithAttachment(
        @RequestParam(name = "emailRequest") String emailRequestDto, 
        @RequestParam(name = "attachment") MultipartFile multipartFile) {
        
        emailService.sendEmailWithAttachment(emailRequestDto, multipartFile);
        
        return ResponseEntity.ok("Email sent with success!!");
    }
    
}
