package com.noc.rest_api.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noc.rest_api.config.EmailConfig;
import com.noc.rest_api.dto.request.EmailRequestDto;
import com.noc.rest_api.mail.EmailSender;

@Service
public class EmailService {
    
    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDto emailRequestDto){
        emailSender
            .to(emailRequestDto.getTo())
            .withSubject(emailRequestDto.getSubject())
            .withMessage(emailRequestDto.getBody())
            .send(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestDto, MultipartFile multipartFile){
        File tempFile = null;
        
        try {
            EmailRequestDto emailRequest = new ObjectMapper().readValue(emailRequestDto, EmailRequestDto.class);
            tempFile = File.createTempFile("attachment", multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile);

            emailSender
                .to(emailRequest.getTo())
                .withSubject(emailRequest.getSubject())
                .withMessage(emailRequest.getBody())
                .attach(tempFile.getAbsolutePath())
                .send(emailConfig);
        } catch (Exception e) {
            throw new RuntimeException("error: " + e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
