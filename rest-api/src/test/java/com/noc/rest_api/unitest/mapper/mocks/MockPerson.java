package com.noc.rest_api.unitest.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import com.noc.rest_api.dto.v1.PersonDto;
import com.noc.rest_api.model.Person;

public class MockPerson {
    
    public Person mockEntity(){
        return mockEntity(0L);
    }

    public PersonDto mockDto(){
        return mockDto(0L);
    }

    public List<Person> mockEntityList(){
        List<Person> persons = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            persons.add(mockEntity(Integer.toUnsignedLong(i)));
        }

        return persons;
    }

    public List<PersonDto> mockDtoList(){
        List<PersonDto> persons = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            persons.add(mockDto(Integer.toUnsignedLong(i)));
        }

        return persons;
    }

    public Person mockEntity(Long id){
        Person person = new Person();
        person.setId(id);
        person.setFirstName("First Name Test " + id);
        person.setLastName("Last Name Test " + id);
        person.setAddress("Address Test " + id);
        person.setGender((id % 2 == 0) ? "Male": "Female");

        return person;
    }

    public PersonDto mockDto(Long id){
        PersonDto personDto = new PersonDto();
        personDto.setId(id);
        personDto.setFirstName("First Name Test " + id);
        personDto.setLastName("Last Name Test " + id);
        personDto.setAddress("Address Test " + id);
        personDto.setGender((id % 2 == 0) ? "Male": "Female");

        return personDto;
    }
}
