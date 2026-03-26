package com.noc.rest_api.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@JsonPropertyOrder({"id", "lastName", "firstName", "birthday", "address", "gender"})
public class PersonDto extends RepresentationModel<PersonDto> {

    @JsonProperty("_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private Boolean enabled;
}
