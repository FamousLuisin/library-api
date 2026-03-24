package com.noc.rest_api.controllers.docs;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.noc.rest_api.dto.BookDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface BookControllerDocs {

    @Operation(
        summary = "find All",
        description = "find All books",
        tags = {"Book"},
        responses = {
            @ApiResponse(description = "Success",
                content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class))
                )},
                responseCode = "200"
            ),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public List<BookDto> findAll();
    
    @Operation(
        summary = "find by ID",
        description = "Find Book by Id",
        tags = {"Book"},
        responses = {
            @ApiResponse(description = "Success",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookDto.class)
                ),
                responseCode = "200"
            ),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public BookDto findById(@PathVariable Long id);
    
    @Operation(
        summary = "create Book",
        description = "Create Book",
        tags = {"Book"},
        responses = {
            @ApiResponse(description = "Success",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookDto.class)
                ),
                responseCode = "200"
            ),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public BookDto create(@RequestBody BookDto request);

    @Operation(
        summary = "update Book",
        description = "Update Book",
        tags = {"Book"},
        responses = {
            @ApiResponse(description = "Success",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookDto.class)
                ),
                responseCode = "200"
            ),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public BookDto update(@RequestBody BookDto request);
    
    @Operation(
        summary = "delete Book by ID",
        description = "Delete Book by ID",
        tags = {"Book"},
        responses = {
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public ResponseEntity<?> delete(@PathVariable Long id);

}