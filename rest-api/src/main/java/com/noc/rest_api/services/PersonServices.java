package com.noc.rest_api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.noc.rest_api.controllers.PersonController;
import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.exception.RequiredObjectIsNullException;
import com.noc.rest_api.mapper.Mapper;
import com.noc.rest_api.model.Person;
import com.noc.rest_api.repository.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonServices {
    
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository pRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    PagedResourcesAssembler<PersonDto> assemnbler;

    public PersonDto findById(Long id){
        logger.info("Find Person by Id " + id);

        Person person = pRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        var dto = mapper.parseObject(person, PersonDto.class);

        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<PersonDto>> findAll(Pageable pageable){
        logger.info("Find all peoples");

        var listPersons = pRepository.findAll(pageable);

        var personWithLink = listPersons.map(person -> {
            var dto = mapper.parseObject(person, PersonDto.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().getOrderFor("firstName").getDirection().toString())).withSelfRel();
        
        return assemnbler.toModel(personWithLink, findAllLink);
    }

    public PagedModel<EntityModel<PersonDto>> findByName(String firstName, Pageable pageable){
        logger.info("Find peoples by name");

        var listPersons = pRepository.findPeopleByName(firstName, pageable);

        var personWithLink = listPersons.map(person -> {
            var dto = mapper.parseObject(person, PersonDto.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().getOrderFor("firstName").getDirection().toString())).withSelfRel();
        
        return assemnbler.toModel(personWithLink, findAllLink);
    }

    public PersonDto create(PersonDto personDto){
        logger.info("Create person");

        if (personDto == null) throw new RequiredObjectIsNullException();

        Person person = pRepository.save(mapper.parseObject(personDto, Person.class));

        var dto = mapper.parseObject(person, PersonDto.class);

        addHateoasLinks(dto);
        return dto;
    }

    public PersonDto update(PersonDto personDto){
        logger.info("Update person");

        if (personDto == null) throw new RequiredObjectIsNullException();

        Person person = pRepository
            .findById(personDto.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")); 
        
        person.setAddress(personDto.getAddress());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setGender(personDto.getGender());

        pRepository.save(person);

        var dto = mapper.parseObject(person, PersonDto.class);

        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDto disablePerson(Long id){
        logger.info("Disable person by ID");
        
        pRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        pRepository.disablePerson(id);

        Person person = pRepository.findById(id).get();

        var dto = mapper.parseObject(person, PersonDto.class);

        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id){
        logger.info("Delete person");
        
        pRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        pRepository.deleteById(id);
    }

    private void addHateoasLinks(PersonDto dto){
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class).findAll(0, 10, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
