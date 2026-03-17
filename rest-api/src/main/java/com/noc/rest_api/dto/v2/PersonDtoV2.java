package com.noc.rest_api.dto.v2;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PersonDtoV2 {

    private Long id;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String address;
    private String gender;
}
