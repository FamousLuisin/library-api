package com.noc.rest_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {
    
    @Autowired
    private PersonServices pServices;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET,
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    public PersonDto findById(@PathVariable("id") Long id){
        var p = pServices.findById(id);
        p.setAddress(null);
        return p;
    }

    @RequestMapping(method = RequestMethod.GET, 
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE  })
    public List<PersonDto> findAll(){

        return pServices.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, 
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    public PersonDto create(@RequestBody PersonDto person){
        return pServices.create(person);
    }

    @RequestMapping(method = RequestMethod.PUT, 
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
        consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    public PersonDto update(@RequestBody PersonDto person){
        return pServices.update(person);
    }

    @RequestMapping(path = "/{id}" ,method = RequestMethod.DELETE, 
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    public void delete(@PathVariable("id") Long id){
        pServices.delete(id);
    }
}
