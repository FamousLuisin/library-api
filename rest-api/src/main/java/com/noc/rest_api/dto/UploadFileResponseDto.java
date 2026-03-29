package com.noc.rest_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class UploadFileResponseDto {
    
    private String fileName;
    private String fileDowloadUri;
    private String fileType;
    private Long size;
}
