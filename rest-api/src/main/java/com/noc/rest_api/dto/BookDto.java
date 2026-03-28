package com.noc.rest_api.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Data @EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"id", "title", "author", "price", "lauchDate"})
@Relation(collectionRelation = "books")
public class BookDto extends RepresentationModel<BookDto>{
    
    private Long id;
    private String author;
    private String title;
    private LocalDate launchDate;
    private Double price;
}
