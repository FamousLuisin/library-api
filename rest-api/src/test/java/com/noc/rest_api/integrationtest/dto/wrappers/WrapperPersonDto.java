package com.noc.rest_api.integrationtest.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class WrapperPersonDto {
    
    @JsonProperty("_embedded")
    private PersonEmbeddedDto embedded;
}
