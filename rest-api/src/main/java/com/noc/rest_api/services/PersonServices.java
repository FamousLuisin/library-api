package com.noc.rest_api.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.noc.rest_api.model.Person;
import com.noc.rest_api.repository.PersonRepository;

@Service
public class PersonServices {
    
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository pRepository;

    public Person findById(Long id){
        logger.info("Find Person by Id " + id);

        return pRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    public List<Person> findAll(){
        logger.info("Find all peoples");

        return pRepository.findAll();
    }

    public Person create(Person person){
        logger.info("Create person");

        pRepository.save(person);

        return person;
    }

    public Person update(Person person){
        logger.info("Update person");

        pRepository
            .findById(person.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return pRepository.save(person);
    }

    public void delete(Long id){
        logger.info("Delete person");
        
        pRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        pRepository.deleteById(id);
    }
}
