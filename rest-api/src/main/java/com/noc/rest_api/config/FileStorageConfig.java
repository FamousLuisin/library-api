package com.noc.rest_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "file")
@AllArgsConstructor @NoArgsConstructor
@Data
public class FileStorageConfig {
    
    private String uploadDir;
}
