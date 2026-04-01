package com.noc.rest_api.integrationtest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.noc.rest_api.model.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@JsonPropertyOrder({"id", "lastName", "firstName", "birthday", "address", "gender"})
public class PersonDto {

    @JsonProperty("_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private Boolean enabled;

    private List<Book> books;
}
