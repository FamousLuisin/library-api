package com.noc.rest_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@AllArgsConstructor @NoArgsConstructor
@Data
public class EmailConfig {
    
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String from;
    private Boolean ssl;
}
