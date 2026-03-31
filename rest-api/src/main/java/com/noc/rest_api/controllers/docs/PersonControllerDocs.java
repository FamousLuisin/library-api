package com.noc.rest_api.controllers.docs;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.file.exporter.MediaTypes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PersonControllerDocs {
    
    @Operation(summary = "Find Person", description = "Find a specific person by your ID", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PersonDto.class)
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
    public PersonDto findById(@PathVariable("id") Long id);

    @Operation(summary = "Create people with file", description = "Create people with file Spreadsheet XLSX or CSV", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = PersonDto.class))
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
    public List<PersonDto> importPeopleWithSpreadsheet(@RequestParam(value = "file") MultipartFile file);

    @Operation(summary = "Export page", description = "Export a page of peoples in XLSX and CSV format", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = {
                    @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE),
                    @Content(mediaType = MediaTypes.APPLICATION_CSV_VALUE)
                }, 
                responseCode = "200"
            ),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public ResponseEntity<Resource> exportPage(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        HttpServletRequest request
    );

    @Operation(summary = "Find All People", description = "Find All Peopel", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = PersonDto.class))
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
    public ResponseEntity<PagedModel<EntityModel<PersonDto>>> findAll(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(summary = "Find Person By Name", description = "Find Person By Name",
        responses = {
            @ApiResponse(description = "OK",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = PersonDto.class))
                ),
                responseCode = "200"),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public ResponseEntity<PagedModel<EntityModel<PersonDto>>> findPersonByName(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        @PathVariable("firstName") String firstName
    );

    @Operation(summary = "Create Person", description = "Create a person", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PersonDto.class)
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
    public PersonDto create(@RequestBody PersonDto person);

    @Operation(summary = "Update Person", description = "Update a person by ID from body", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PersonDto.class)
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
    public PersonDto update(@RequestBody PersonDto person);

    @Operation(summary = "Disable Person", description = "Disable a specific person by your ID", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PersonDto.class)
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
    public PersonDto disablePerson(@PathVariable("id") Long id);

    @Operation(summary = "Delete Person", description = "Delete Person by your ID", 
        tags = {"People"},
        responses = {
            @ApiResponse(description = "Success", 
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PersonDto.class)
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
    public ResponseEntity<?> delete(@PathVariable("id") Long id);
}
