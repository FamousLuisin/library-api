package com.noc.rest_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Data
public class EmailRequestDto {
    
    private String to;
    private String subject;
    private String body;
}
