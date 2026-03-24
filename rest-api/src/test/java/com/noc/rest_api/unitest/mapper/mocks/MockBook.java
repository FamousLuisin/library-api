package com.noc.rest_api.unitest.mapper.mocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.noc.rest_api.dto.BookDto;
import com.noc.rest_api.model.Book;

public class MockBook {
    
    public Book mockEntity(){
        return mockEntity(0L);
    }

    public BookDto mockDto(){
        return mockDto(0L);
    }

    public List<Book> mockEntityList(){
        List<Book> books = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            books.add(mockEntity(Integer.toUnsignedLong(i)));
        }

        return books;
    }

    public List<BookDto> mockDtoList(){
        List<BookDto> books = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            books.add(mockDto(Integer.toUnsignedLong(i)));
        }

        return books;
    }

    public Book mockEntity(Long id){
        Book book = new Book();

        book.setId(id);
        book.setAuthor("Author Test " + id);
        book.setLaunchDate(LocalDate.now());
        book.setPrice(75D);
        book.setTitle("Title Test " + id);

        return book;
    }

    public BookDto mockDto(Long id){
        BookDto book = new BookDto();

        book.setId(id);
        book.setAuthor("Author Test " + id);
        book.setLaunchDate(LocalDate.now());
        book.setPrice(75D);
        book.setTitle("Title Test " + id);

        return book;
    }
}
