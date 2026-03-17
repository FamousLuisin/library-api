package com.noc.rest_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PersonDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private String gender;
}
