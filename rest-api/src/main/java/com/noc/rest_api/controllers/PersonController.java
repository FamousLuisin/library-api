package com.noc.rest_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noc.rest_api.controllers.docs.PersonControllerDocs;
import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public List<PersonDto> findAll(){

        return pServices.findAll();
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

    @PutMapping(
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public PersonDto update(@RequestBody PersonDto person){
        return pServices.update(person);
    }

    @DeleteMapping(path = "/{id}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        pServices.delete(id);

        return ResponseEntity.noContent().build();
    }
}
