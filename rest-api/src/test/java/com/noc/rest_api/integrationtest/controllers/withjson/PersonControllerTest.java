package com.noc.rest_api.integrationtest.controllers.withjson;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noc.rest_api.config.TestConfigs;
import com.noc.rest_api.integrationtest.dto.PersonDto;
import com.noc.rest_api.integrationtest.dto.wrappers.WrapperPersonDto;
import com.noc.rest_api.integrationtest.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDto person;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDto();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        specification = new  RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_VALID)
            .setBasePath("/api/person")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(person)
			.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
        
        PersonDto createdPerson = objectMapper.readValue(content, PersonDto.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() >= 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("EUA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        person.setAddress("Finland");

        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(person)
			.when()
				.put()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
        
        PersonDto createdPerson = objectMapper.readValue(content, PersonDto.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() >= 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
    
        PersonDto createdPerson = objectMapper.readValue(content, PersonDto.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() >= 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testDisable() throws JsonProcessingException {
        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", person.getId())
			.when()
				.patch("{id}")
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();
    
        PersonDto createdPerson = objectMapper.readValue(content, PersonDto.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() >= 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void testDelete() throws JsonProcessingException {
        given(specification)
            .pathParam("id", person.getId())
			.when()
				.delete("{id}")
			.then()
				.statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() throws JsonProcessingException {
        String content = given(specification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", 10, "size", 10, "direction", "asc")
			.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();

        WrapperPersonDto wrapper = objectMapper.readValue(content, WrapperPersonDto.class);
        List<PersonDto> people = wrapper.getEmbedded().getPeople();
    
        PersonDto firstPerson = people.get(0);

        System.out.println(firstPerson);

        assertNotNull(firstPerson.getId());
        assertTrue(firstPerson.getId() == 152);

        assertEquals("Basia", firstPerson.getFirstName());
        assertEquals("Sorsby", firstPerson.getLastName());
        assertEquals("Sos. Fabrica de Glucoza nr.5, Business Center, Novo Park 3, cladirea F, et.5 si 6, sector 2", firstPerson.getAddress());
        assertEquals("Female", firstPerson.getGender());
        assertFalse(firstPerson.getEnabled());

        PersonDto tenthPerson = people.get(9);

        assertNotNull(tenthPerson.getId());
        assertTrue(tenthPerson.getId() == 593);

        assertEquals("Benedict", tenthPerson.getFirstName());
        assertEquals("Counter", tenthPerson.getLastName());
        assertEquals("BOX 70", tenthPerson.getAddress());
        assertEquals("Male", tenthPerson.getGender());
        assertTrue(tenthPerson.getEnabled());
    }

    @Test
    @Order(7)
    void testFindByName() throws JsonProcessingException {
        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("name", "ti")
            .queryParam("page", 1, "size", 10, "direction", "asc")
            .when()
                .get("/findByName/{name}")
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();
        
        WrapperPersonDto wrapper = objectMapper.readValue(content, WrapperPersonDto.class);
        List<PersonDto> people = wrapper.getEmbedded().getPeople();

        PersonDto sixthPerson = people.get(5);

        assertNotNull(sixthPerson.getId());
        assertTrue(sixthPerson.getId() == 194);

        assertEquals("Justina", sixthPerson.getFirstName());
        assertEquals("Pedrick", sixthPerson.getLastName());
        assertEquals("Floor 1, Saint Stephen's Green House, Earlsfort Terrace,", sixthPerson.getAddress());
        assertEquals("Female", sixthPerson.getGender());
        assertFalse(sixthPerson.getEnabled());
        

        PersonDto tenthPerson = people.get(9);

        assertNotNull(tenthPerson.getId());
        assertTrue(tenthPerson.getId() == 994);

        assertEquals("Konstantine", tenthPerson.getFirstName());
        assertEquals("Shakesby", tenthPerson.getLastName());
        assertEquals("Mühlenstraße 8", tenthPerson.getAddress());
        assertEquals("Male", tenthPerson.getGender());
        assertTrue(tenthPerson.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("EUA");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
