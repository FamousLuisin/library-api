package com.noc.rest_api.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class AccountCredentialsDto {
    
    private String username;
    private String fullname;
    private String password;
}
