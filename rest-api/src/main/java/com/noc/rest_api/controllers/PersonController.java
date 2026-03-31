package com.noc.rest_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.noc.rest_api.controllers.docs.PersonControllerDocs;
import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.file.exporter.MediaTypes;
import com.noc.rest_api.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {
    
    @Autowired
    private PersonServices pServices;

    // @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(path = "/{id}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    @Override
    public PersonDto findById(@PathVariable("id") Long id){

        return pServices.findById(id);
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDto>>> findAll(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.status(HttpStatus.OK).body(pServices.findAll(pageable));
    }

    @GetMapping(
        path = "/findByName/{firstName}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDto>>> findPersonByName(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        @PathVariable("firstName") String firstName
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.status(HttpStatus.OK).body(pServices.findByName(firstName, pageable));
    }

    // @CrossOrigin(origins = {"http://localhost:8080", "http://google.com.br"})
    @PostMapping(
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public PersonDto create(@RequestBody PersonDto person){
        return pServices.create(person);
    }

    @PostMapping(
        path = "/spreadsheet",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public List<PersonDto> importPeopleWithSpreadsheet(@RequestParam(value = "file") MultipartFile file){
        return pServices.createWithSpreadsheet(file);
    }

    @GetMapping(
        path = "/spreadsheet",
        produces = {
            MediaTypes.APPLICATION_XLSX_VALUE,
            MediaTypes.APPLICATION_CSV_VALUE
        }
    )
    @Override
    public ResponseEntity<Resource> exportPage(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        HttpServletRequest request
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        
        Resource file = pServices.exportPeople(pageable, acceptHeader);
        
        String contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";
        String fileExtension = MediaTypes.APPLICATION_XLSX_VALUE.equalsIgnoreCase(acceptHeader) ? ".xlsx" : ".csv";
        String fileName = "people_export" + fileExtension;

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .body(file);
    }

    @PutMapping(
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public PersonDto update(@RequestBody PersonDto person){
        return pServices.update(person);
    }

    @PatchMapping(
        path = "/{id}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public PersonDto disablePerson(@PathVariable("id") Long id) {
        return pServices.disablePerson(id);
    }

    @DeleteMapping(path = "/{id}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        pServices.delete(id);

        return ResponseEntity.noContent().build();
    }
}
