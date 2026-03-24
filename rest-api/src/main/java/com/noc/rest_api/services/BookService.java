package com.noc.rest_api.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    public List<BookDto> findAll(){
        logger.info("find all Books");

        List<Book> books = bRepository.findAll();

        List<BookDto> dto = mapper.parseListObjects(books, BookDto.class);

        dto.forEach(this::addHateoasLinks);

        return dto;
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

        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
