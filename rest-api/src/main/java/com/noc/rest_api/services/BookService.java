package com.noc.rest_api.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.noc.rest_api.controllers.BookController;
import com.noc.rest_api.dto.BookDto;
import com.noc.rest_api.mapper.Mapper;
import com.noc.rest_api.model.Book;
import com.noc.rest_api.repository.BookRepository;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private PagedResourcesAssembler<BookDto> assembler;

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    public PagedModel<EntityModel<BookDto>> findAll(Pageable pageable){
        logger.info("find all Books");

        Page<Book> books = bRepository.findAll(pageable);

        Page<BookDto> dto = books.map(book -> {
            BookDto bookDto = mapper.parseObject(book, BookDto.class);
            addHateoasLinks(bookDto);
            return bookDto;
        });

        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class)
            .findAll(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().getOrderFor("title").getDirection().toString()))
            .withSelfRel();

        return assembler.toModel(dto, link);
    }

    public BookDto findById(Long id){
        logger.info("find Book by id");

        Book book = bRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));

        BookDto dto = mapper.parseObject(book, BookDto.class);
        addHateoasLinks(dto);

        return dto;
    }

    public BookDto create(BookDto dto){
        logger.info("create Book");

        Book book = mapper.parseObject(dto, Book.class);
        bRepository.save(book);

        dto = mapper.parseObject(book, BookDto.class);
        addHateoasLinks(dto);

        return dto;
    }

    public BookDto update(BookDto dto){
        logger.info("update book");

        Book book = bRepository.findById(dto.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));

        book.setAuthor(dto.getAuthor());
        book.setLaunchDate(dto.getLaunchDate());
        book.setPrice(dto.getPrice());
        book.setTitle(dto.getTitle());

        bRepository.save(book);

        dto = mapper.parseObject(book, BookDto.class);

        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id){
        logger.info("delete book");

        Book book = bRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "book not found"));

        bRepository.delete(book);
    }

    public void addHateoasLinks(BookDto dto){
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));

        dto.add(linkTo(methodOn(BookController.class).findAll(0, 10, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
