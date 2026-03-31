package com.noc.rest_api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.noc.rest_api.controllers.PersonController;
import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.exception.FileStorageException;
import com.noc.rest_api.exception.RequiredObjectIsNullException;
import com.noc.rest_api.file.exporter.contract.FileExporter;
import com.noc.rest_api.file.exporter.factory.FileExporterFactory;
import com.noc.rest_api.file.importer.contract.FileImporter;
import com.noc.rest_api.file.importer.factory.FileImporterFactory;
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
    private FileImporterFactory importerFactory;

    @Autowired
    private FileExporterFactory exporterFactory;

    @Autowired
    PagedResourcesAssembler<PersonDto> assembler;

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
        
        return buildPagedModel(pageable, listPersons);
    }

    public Resource exportPeople(Pageable pageable, String acceptHeader){
        logger.info("Exporting a People page!");

        var listPersons = pRepository.findAll(pageable)
            .map(p -> mapper.parseObject(p, PersonDto.class))
            .getContent();

        try {
            FileExporter exporter = this.exporterFactory.getExporter(acceptHeader);

            return exporter.exportFile(listPersons);
        } catch (Exception e) {
            logger.error("Error during file export!", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during file export!");
        }
    }

    public PagedModel<EntityModel<PersonDto>> findByName(String firstName, Pageable pageable){
        logger.info("Find peoples by name");

        var listPersons = pRepository.findPeopleByName(firstName, pageable);
        
        return buildPagedModel(pageable, listPersons);
    }

    public PersonDto create(PersonDto personDto){
        logger.info("Create person");

        if (personDto == null) throw new RequiredObjectIsNullException();

        Person person = pRepository.save(mapper.parseObject(personDto, Person.class));

        var dto = mapper.parseObject(person, PersonDto.class);

        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDto> createWithSpreadsheet(MultipartFile file) {
        logger.info("Import peoples from file");

        if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plase set a valid File");
        
        try (InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fila name cannot be null"));

            FileImporter importer = this.importerFactory.getImporter(filename);

            List<Person> entities = importer.importFile(inputStream).stream().map(dto -> mapper.parseObject(dto, Person.class)).toList();

            entities = pRepository.saveAll(entities);

            List<PersonDto> entitiesDto = mapper.parseListObjects(entities, PersonDto.class);
            entitiesDto.forEach(this::addHateoasLinks);

            return entitiesDto;
        } catch (Exception e){
            logger.error("" + e);
            throw new FileStorageException("Error processing the file");
        }
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
        dto.add(linkTo(methodOn(PersonController.class).findPersonByName(0, 10, "asc", ""))
            .withRel("findByName")
            .withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(0, 10, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("spreadsheet").withRel("createWithSpreadsheet").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(1, 12, "asc", null))
            .withRel("exportPage")
            .withType("GET"));
    }

    private PagedModel<EntityModel<PersonDto>> buildPagedModel(Pageable pageable, Page<Person> people){
        var personWithLink = people.map(person -> {
            var dto = mapper.parseObject(person, PersonDto.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), 
                        pageable.getPageSize(), 
                        pageable.getSort().getOrderFor("firstName").getDirection().toString())).withSelfRel();
        
        return assembler.toModel(personWithLink, findAllLink);
    }
}
