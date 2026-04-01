package com.noc.rest_api.integrationtest.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noc.rest_api.config.TestConfigs;
import com.noc.rest_api.integrationtest.dto.BookDto;
import com.noc.rest_api.integrationtest.dto.wrappers.WrapperBookDto;
import com.noc.rest_api.integrationtest.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class BookControllerTest extends AbstractIntegrationTest {
    
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookDto book;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());

        book = new BookDto();
    }
    
    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockBook();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_VALID)
            .setBasePath("/api/book")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(book)
			.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();

        BookDto createdBook = objectMapper.readValue(content, BookDto.class);
        book = createdBook;

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());

        assertEquals(216, createdBook.getId());
        assertEquals("Anthony", createdBook.getAuthor());
        assertEquals(20D, createdBook.getPrice());
        assertEquals("A Clockwork Orange", createdBook.getTitle());
        assertEquals(LocalDate.of(1962, 3, 17), createdBook.getLaunchDate());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        book.setAuthor("Anthony Burgess");

        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(book)
            .when()
                .put()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();
        
        BookDto createdBook = objectMapper.readValue(content, BookDto.class);

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());

        assertEquals(216, createdBook.getId());
        assertEquals("Anthony Burgess", createdBook.getAuthor());
        assertEquals(20D, createdBook.getPrice());
        assertEquals("A Clockwork Orange", createdBook.getTitle());
        assertEquals(LocalDate.of(1962, 3, 17), createdBook.getLaunchDate());
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", book.getId())
            .when()
                .get("/{id}")
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();
        
        BookDto createdBook = objectMapper.readValue(content, BookDto.class);

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());

        assertEquals(216, createdBook.getId());
        assertEquals("Anthony Burgess", createdBook.getAuthor());
        assertEquals(20D, createdBook.getPrice());
        assertEquals("A Clockwork Orange", createdBook.getTitle());
        assertEquals(LocalDate.of(1962, 3, 17), createdBook.getLaunchDate());
    }

    @Test
    @Order(4)
    void testFindAll() throws JsonProcessingException {
        String content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", 1, "size", 10, "direction", "asc")
            .when()
                .get()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();
        
        WrapperBookDto wrapper = objectMapper.readValue(content, WrapperBookDto.class);
        List<BookDto> books = wrapper.getEmbedded().getBooks();

        BookDto firstBook = books.get(0);

        assertNotNull(firstBook);
        assertNotNull(firstBook.getId());

        assertEquals(27, firstBook.getId());
        assertEquals("Craig Larman", firstBook.getAuthor());
        assertEquals(43.82D, firstBook.getPrice());
        assertEquals("Agile and Iterative Development: A Manager’s Guide", firstBook.getTitle());
        assertEquals(LocalDate.of(1998, 12, 5), firstBook.getLaunchDate());

        BookDto lastBook = books.get(6);

        assertNotNull(lastBook);
        assertNotNull(lastBook.getId());

        assertEquals(12, lastBook.getId());
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", lastBook.getAuthor());
        assertEquals(54D, lastBook.getPrice());
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", lastBook.getTitle());
        assertEquals(LocalDate.of(2017, 11, 7), lastBook.getLaunchDate());
    }

    @Test
    @Order(5)
    void testDelete() {
        given(specification)
            .pathParam("id", book.getId())
            .when()
                .delete("/{id}")
            .then()
                .statusCode(204);
    }

    private void mockBook(){
        book.setAuthor("Anthony");
        book.setPrice(20D);
        book.setTitle("A Clockwork Orange");
        book.setLaunchDate(LocalDate.of(1962, 3, 17));
    }
}
