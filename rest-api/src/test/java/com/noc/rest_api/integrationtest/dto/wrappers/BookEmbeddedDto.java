package com.noc.rest_api.integrationtest.dto.wrappers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noc.rest_api.integrationtest.dto.BookDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class BookEmbeddedDto {
    
    @JsonProperty("books")
    private List<BookDto> books;
}
