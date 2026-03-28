package com.noc.rest_api.integrationtest.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Data
public class BookDto {
    
    private Long id;
    private String author;
    private String title;
    private LocalDate launchDate;
    private Double price;
}
