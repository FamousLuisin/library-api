package com.noc.rest_api.dto.security;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class TokenDto {
    
    private String username;
    private Boolean authenticated;
    private Instant created;
    private Instant expiration;
    private String accesToken;
    private String refreshToken;
}
