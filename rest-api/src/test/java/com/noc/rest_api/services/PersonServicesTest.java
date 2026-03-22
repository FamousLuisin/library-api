package com.noc.rest_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.exception.RequiredObjectIsNullException;
import com.noc.rest_api.mapper.Mapper;
import com.noc.rest_api.model.Person;
import com.noc.rest_api.repository.PersonRepository;
import com.noc.rest_api.unitest.mapper.mocks.MockPerson;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices services;

    @Mock
    private Mapper mapper;

    @Mock
    private PersonRepository pRepository;

    @BeforeEach
    void setUp(){
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Person person = input.mockEntity(1L);
        Person persisted = person;
        PersonDto dto = input.mockDto(1L);

        when(pRepository.save(person)).thenReturn(persisted);
        when(mapper.parseObject(dto, Person.class)).thenReturn(person);
        when(mapper.parseObject(persisted, PersonDto.class)).thenReturn(dto);

        var result = services.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getGender(), result.getGender());
    }

    @Test
    void testCreateWithNullPerson(){
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> {
            services.create(null);
        });

        String expetedMessage = "It is not allowed to persist a null object";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expetedMessage));
    }

    @Test
    void testDelete() {
        Person person = input.mockEntity(1L);
        when(pRepository.findById(1L)).thenReturn(Optional.of(person));

        services.delete(1L);
        verify(pRepository, times(1)).findById(anyLong());
        verify(pRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(pRepository);
    }

    @Test
    void testFindAll() {
        List<Person> persons = input.mockEntityList();
        List<PersonDto> dto = input.mockDtoList();
        when(pRepository.findAll()).thenReturn(persons);
        when(mapper.parseListObjects(persons, PersonDto.class)).thenReturn(dto);
        var result = services.findAll();

        assertNotNull(result);
        assertEquals(15, result.size());

        PersonDto personOne = result.get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getId());
        assertNotNull(personOne.getLinks());

        assertTrue(personOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("GET")
        ));

        assertTrue(personOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person")
            && link.getType().equals("GET")
        ));

        assertTrue(personOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(personOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(personOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals("Address Test 1", personOne.getAddress());
        assertEquals("First Name Test 1", personOne.getFirstName());
        assertEquals("Last Name Test 1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());


        PersonDto personFour = result.get(4);

        assertNotNull(personFour);
        assertNotNull(personFour.getId());
        assertNotNull(personFour.getLinks());

        assertTrue(personFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/4")
            && link.getType().equals("GET")
        ));

        assertTrue(personFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person")
            && link.getType().equals("GET")
        ));

        assertTrue(personFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(personFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(personFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/4")
            && link.getType().equals("DELETE")
        ));

        assertEquals("Address Test 4", personFour.getAddress());
        assertEquals("First Name Test 4", personFour.getFirstName());
        assertEquals("Last Name Test 4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());

    }

    @Test
    void testFindById() {
        Person person = input.mockEntity(1L);
        PersonDto dto = input.mockDto(1L);
        when(pRepository.findById(1L)).thenReturn(Optional.of(person));
        when(mapper.parseObject(person, PersonDto.class)).thenReturn(dto);
        var result = services.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getGender(), result.getGender());
    }

    @Test
    void testUpdate() {
        Person person = input.mockEntity(1L);
        PersonDto dto = input.mockDto(1L);

        when(pRepository.findById(1L)).thenReturn(Optional.of(person));
        when(pRepository.save(person)).thenReturn(person);
        when(mapper.parseObject(person, PersonDto.class)).thenReturn(dto);

        var result = services.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getGender(), result.getGender());
    }

    @Test
    void testUpdateWithNullPerson(){
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> {
            services.update(null);
        });

        String expetedMessage = "It is not allowed to persist a null object";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expetedMessage));
    }

}
