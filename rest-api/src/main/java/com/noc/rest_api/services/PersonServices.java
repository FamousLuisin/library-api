package com.noc.rest_api.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.noc.rest_api.dto.v1.PersonDto;
import com.noc.rest_api.dto.v2.PersonDtoV2;
import com.noc.rest_api.mapper.Mapper;
import com.noc.rest_api.model.Person;
import com.noc.rest_api.repository.PersonRepository;

@Service
public class PersonServices {
    
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository pRepository;

    @Autowired
    private Mapper mapper;

    public PersonDto findById(Long id){
        logger.info("Find Person by Id " + id);

        Person person = pRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return mapper.parseObject(person, PersonDto.class);
    }

    public List<PersonDto> findAll(){
        logger.info("Find all peoples");

        List<Person> listPersons = pRepository.findAll();

        return mapper.parseListObjects(listPersons, PersonDto.class);
    }

    public PersonDto create(PersonDto personDto){
        logger.info("Create person");

        Person person = pRepository.save(mapper.parseObject(personDto, Person.class));

        personDto.setId(person.getId());

        return personDto;
    }

    public PersonDtoV2 create(PersonDtoV2 personDto){
        logger.info("Create person");

        Person person = pRepository.save(mapper.parseObject(personDto, Person.class));

        personDto.setId(person.getId());

        return personDto;
    }

    public PersonDto update(PersonDto personDto){
        logger.info("Update person");

        Person person = pRepository
            .findById(personDto.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")); 
        
        person.setAddress(personDto.getAddress());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setGender(personDto.getGender());

        return mapper.parseObject(pRepository.save(person) , PersonDto.class);
    }

    public void delete(Long id){
        logger.info("Delete person");
        
        pRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        pRepository.deleteById(id);
    }
}
