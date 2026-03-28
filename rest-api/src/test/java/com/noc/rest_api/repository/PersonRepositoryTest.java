package com.noc.rest_api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.noc.rest_api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.noc.rest_api.model.Person;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository pRepository;
    private static Person person;

    @BeforeAll
    static void setUp(){
        person = new Person();
    }

    @Test
    @Order(1)
    void testFindPeopleByName() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName"));

        person = pRepository.findPeopleByName("fons", pageable).getContent().get(1);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Alfons", person.getFirstName());
        assertEquals("Januszewicz", person.getLastName());
        assertEquals("801 E CALIFORNIA ST", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }


    @Test
    @Order(2)
    void testDisablePerson() {

        Long id = person.getId();
        pRepository.disablePerson(id);

        var result = pRepository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Alfons", person.getFirstName());
        assertEquals("Januszewicz", person.getLastName());
        assertEquals("801 E CALIFORNIA ST", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }

}   
