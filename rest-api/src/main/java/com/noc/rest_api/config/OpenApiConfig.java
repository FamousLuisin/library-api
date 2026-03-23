package com.noc.rest_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .info(new Info()
                .title("REST API")
                .version("V1")
                .description("REST API with Java, SPring Boot, Kubernetes and Docker")
                .license(new License()
                    .name("MIT License")
                    .url("https://mit-license.org/")
                )
            );
    }
}
