package com.noc.rest_api.unitest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.noc.rest_api.dto.v1.PersonDto;
import com.noc.rest_api.mapper.Mapper;
import com.noc.rest_api.model.Person;
import com.noc.rest_api.unitest.mapper.mocks.MockPerson;

public class MapperTest {
    
    MockPerson mock;
    Mapper mapper;

    @BeforeEach
    public void setUp(){
        this.mock = new MockPerson();
        this.mapper = new Mapper();
    }

    @Test
    public void parseEntityToDtoTest(){
        PersonDto output = mapper.parseObject(mock.mockEntity(), PersonDto.class);
        assertEquals(0L, output.getId());
        assertEquals("First Name Test 0", output.getFirstName());
        assertEquals("Last Name Test 0", output.getLastName());
        assertEquals("Address Test 0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parseEntityListToDtoListTest(){
        List<PersonDto> outputList = mapper.parseListObjects(mock.mockEntityList(), PersonDto.class);

        for (int i = 0; i < outputList.size(); i++) {
            assertEquals(i, outputList.get(i).getId());
            assertEquals("First Name Test " + i, outputList.get(i).getFirstName());
            assertEquals("Last Name Test " + i, outputList.get(i).getLastName());
            assertEquals("Address Test " + i, outputList.get(i).getAddress());
            assertEquals((i % 2 == 0 ? "Male" : "Female"), outputList.get(i).getGender());
        }
    }

    @Test
    public void parseDtoToEntityTest(){
        Person output = mapper.parseObject(mock.mockDto(), Person.class);

        assertEquals(0L, output.getId());
        assertEquals("First Name Test 0", output.getFirstName());
        assertEquals("Last Name Test 0", output.getLastName());
        assertEquals("Address Test 0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parseDtoListToEntityListTest(){
        List<Person> outputList = mapper.parseListObjects(mock.mockDtoList(), Person.class);

        for (int i = 0; i < outputList.size(); i++) {
            assertEquals(i, outputList.get(i).getId());
            assertEquals("First Name Test " + i, outputList.get(i).getFirstName());
            assertEquals("Last Name Test " + i, outputList.get(i).getLastName());
            assertEquals("Address Test " + i, outputList.get(i).getAddress());
            assertEquals((i % 2 == 0 ? "Male" : "Female"), outputList.get(i).getGender());
        }
    }
}
