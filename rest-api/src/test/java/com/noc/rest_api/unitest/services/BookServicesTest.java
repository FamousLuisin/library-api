package com.noc.rest_api.unitest.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

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

    @Mock
    PagedResourcesAssembler<BookDto> assembler;

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
    void testFindAll() {
        List<Book> books = mock.mockEntityList();
        List<BookDto> dto = mock.mockDtoList();
        Page<Book> mockPage = new PageImpl<>(books);
        
        when(bRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Iterator<BookDto> iterator = dto.iterator();
        
        when(mapper.parseObject(any(Book.class), eq(BookDto.class)))
            .thenAnswer(invocation -> iterator.next());

        List<EntityModel<BookDto>> entityModels = dto.stream()
            .map(EntityModel::of)
            .collect(Collectors.toList());

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
            mockPage.getSize(),
            mockPage.getNumber(),
            mockPage.getTotalElements(),
            mockPage.getTotalPages()
        );

        PagedModel<EntityModel<BookDto>> mockPagedModel = PagedModel.of(entityModels, pageMetadata);
        when(assembler.toModel(ArgumentMatchers.<Page<BookDto>>any(), ArgumentMatchers.any(Link.class))).thenReturn(mockPagedModel);

        PagedModel<EntityModel<BookDto>> result = services.findAll(PageRequest.of(0, 15, Sort.by(Sort.Direction.ASC, "title")));

        List<BookDto> booksDto = result.getContent()
            .stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());

        assertNotNull(booksDto);
        assertEquals(15, booksDto.size());

        validateIndividualBook(booksDto.get(1), 1);
        validateIndividualBook(booksDto.get(4), 4);
        validateIndividualBook(booksDto.get(7), 7);
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

    private static void validateIndividualBook(BookDto book, int i) {
        assertNotNull(book);
        assertNotNull(book.getId());
        assertNotNull(book.getLinks());

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/book/v1/" + i)
                        && link.getType().equals("GET")
                ));

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/book/v1")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(book.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/book/v1/" + i)
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Author Test " + i, book.getAuthor());
        assertEquals(75D, book.getPrice());
        assertEquals("Title Test " + i, book.getTitle());
        assertNotNull(book.getLaunchDate());
    }
}
