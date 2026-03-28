package com.noc.rest_api.unitest.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.noc.rest_api.dto.BookDto;
import com.noc.rest_api.mapper.Mapper;
import com.noc.rest_api.model.Book;
import com.noc.rest_api.repository.BookRepository;
import com.noc.rest_api.services.BookService;
import com.noc.rest_api.unitest.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {
    
    @InjectMocks
    private BookService services;

    @Mock
    private BookRepository bRepository;

    @Mock
    private Mapper mapper;

    private MockBook mock;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mock = new MockBook();
    }

    @Test
    void testCreate() {
        Book book = mock.mockEntity(1L);
        Book persisted = book;
        BookDto dto = mock.mockDto(1L);

        when(bRepository.save(book)).thenReturn(persisted);
        when(mapper.parseObject(dto, Book.class)).thenReturn(book);
        when(mapper.parseObject(persisted, BookDto.class)).thenReturn(dto);

        var result = services.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/book?page=0&size=10&direction=asc")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/book")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/book")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals("Author Test 1", result.getAuthor());
        assertEquals("Title Test 1", result.getTitle());
        assertEquals(LocalDate.now(), result.getLaunchDate());
        assertEquals(75D, result.getPrice());
    }

    @Test
    void testDelete() {
        Book book = mock.mockEntity(1L);
        when(bRepository.findById(1L)).thenReturn(Optional.of(book));

        services.delete(1L);
        verify(bRepository, times(1)).findById(anyLong());
        verify(bRepository, times(1)).delete(book);
        verifyNoMoreInteractions(bRepository);
    }

    @Test
    @Disabled("REASON: Still Under Development")
    void testFindAll() {
        List<Book> book = mock.mockEntityList();
        List<BookDto> dto = mock.mockDtoList();
        when(bRepository.findAll()).thenReturn(book);
        when(mapper.parseListObjects(book, BookDto.class)).thenReturn(dto);
        List<BookDto> result = new ArrayList<>();

        assertNotNull(result);
        assertEquals(15, result.size());

        BookDto bookOne = result.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        assertTrue(bookOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("GET")
        ));

        assertTrue(bookOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/book")
            && link.getType().equals("GET")
        ));

        assertTrue(bookOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/book")
            && link.getType().equals("POST")
        ));

        assertTrue(bookOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/book")
            && link.getType().equals("PUT")
        ));

        assertTrue(bookOne.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals("Author Test 1", bookOne.getAuthor());
        assertEquals("Title Test 1", bookOne.getTitle());
        assertEquals(LocalDate.now(), bookOne.getLaunchDate());
        assertEquals(75D, bookOne.getPrice());


        BookDto bookFour = result.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getLinks());

        assertTrue(bookFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/book/4")
            && link.getType().equals("GET")
        ));

        assertTrue(bookFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/book")
            && link.getType().equals("GET")
        ));

        assertTrue(bookFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/book")
            && link.getType().equals("POST")
        ));

        assertTrue(bookFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/book")
            && link.getType().equals("PUT")
        ));

        assertTrue(bookFour.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/book/4")
            && link.getType().equals("DELETE")
        ));

        assertEquals("Author Test 4", bookFour.getAuthor());
        assertEquals("Title Test 4", bookFour.getTitle());
        assertEquals(LocalDate.now(), bookFour.getLaunchDate());
        assertEquals(75D, bookFour.getPrice());
    }

    @Test
    void testFindById() {
        Book book = mock.mockEntity(1L);
        BookDto dto = mock.mockDto(1L);
        when(bRepository.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.parseObject(book, BookDto.class)).thenReturn(dto);
        var result = services.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/book?page=0&size=10&direction=asc")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/book")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/book")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals("Author Test 1", book.getAuthor());
        assertEquals("Title Test 1", book.getTitle());
        assertEquals(LocalDate.now(), book.getLaunchDate());
        assertEquals(75D, book.getPrice());
    }

    @Test
    void testUpdate() {
        Book book = mock.mockEntity(1L);
        BookDto dto = mock.mockDto(1L);

        when(bRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bRepository.save(book)).thenReturn(book);
        when(mapper.parseObject(book, BookDto.class)).thenReturn(dto);

        var result = services.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/book?page=0&size=10&direction=asc")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/book")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/book")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/book/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals("Author Test 1", book.getAuthor());
        assertEquals("Title Test 1", book.getTitle());
        assertEquals(LocalDate.now(), book.getLaunchDate());
        assertEquals(75D, book.getPrice());
    }
}
