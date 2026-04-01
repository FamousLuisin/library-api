package com.noc.rest_api.unitest.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.exception.RequiredObjectIsNullException;
import com.noc.rest_api.mapper.Mapper;
import com.noc.rest_api.model.Person;
import com.noc.rest_api.repository.PersonRepository;
import com.noc.rest_api.services.PersonServices;
import com.noc.rest_api.unitest.mapper.mocks.MockPerson;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices services;

    @Mock
    private Mapper mapper;

    @Mock
    private PersonRepository pRepository;

    @Mock
    PagedResourcesAssembler<PersonDto> assembler;

    @BeforeEach
    void setUp(){
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Person person = input.mockEntity(1L);
        Person persisted = person;
        PersonDto dto = input.mockDto(1L);

        when(pRepository.save(person)).thenReturn(persisted);
        when(mapper.parseObject(dto, Person.class)).thenReturn(person);
        when(mapper.parseObject(persisted, PersonDto.class)).thenReturn(dto);

        var result = services.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person?page=0&size=10&direction=asc")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getGender(), result.getGender());
    }

    @Test
    void testCreateWithNullPerson(){
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> {
            services.create(null);
        });

        String expetedMessage = "It is not allowed to persist a null object";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expetedMessage));
    }

    @Test
    void testDelete() {
        Person person = input.mockEntity(1L);
        when(pRepository.findById(1L)).thenReturn(Optional.of(person));

        services.delete(1L);
        verify(pRepository, times(1)).findById(anyLong());
        verify(pRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(pRepository);
    }

    @Test
    void testFindAll() {
        List<Person> persons = input.mockEntityList();
        List<PersonDto> dto = input.mockDtoList();
        Page<Person> mockPage = new PageImpl<>(persons);
        AtomicInteger index = new AtomicInteger();

        when(pRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        when(mapper.parseObject(any(Person.class), eq(PersonDto.class)))
            .thenAnswer(invocation -> dto.get(index.getAndIncrement()));

        List<EntityModel<PersonDto>> entityModels = dto.stream()
            .map(EntityModel::of)
            .collect(Collectors.toList());

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
            mockPage.getSize(),
            mockPage.getNumber(),
            mockPage.getTotalElements(),
            mockPage.getTotalPages()
        );

        PagedModel<EntityModel<PersonDto>> mockPagedModel = PagedModel.of(entityModels, pageMetadata);
        when(assembler.toModel(ArgumentMatchers.<Page<PersonDto>>any(), ArgumentMatchers.any(Link.class))).thenReturn(mockPagedModel);

        PagedModel<EntityModel<PersonDto>> result = services.findAll(PageRequest.of(0, 15, Sort.by(Sort.Direction.ASC, "firstName")));

        List<PersonDto> people = result.getContent()
            .stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());

        assertNotNull(people);
        assertEquals(15, people.size());

        validateIndividualPerson(people.get(1), 1);
        validateIndividualPerson(people.get(4), 4);
        validateIndividualPerson(people.get(7), 7);
    }

    @Test
    void testFindById() {
        Person person = input.mockEntity(1L);
        PersonDto dto = input.mockDto(1L);
        when(pRepository.findById(1L)).thenReturn(Optional.of(person));
        when(mapper.parseObject(person, PersonDto.class)).thenReturn(dto);
        var result = services.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person?page=0&size=10&direction=asc")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getGender(), result.getGender());
    }

    @Test
    void testUpdate() {
        Person person = input.mockEntity(1L);
        PersonDto dto = input.mockDto(1L);

        when(pRepository.findById(1L)).thenReturn(Optional.of(person));
        when(pRepository.save(person)).thenReturn(person);
        when(mapper.parseObject(person, PersonDto.class)).thenReturn(dto);

        var result = services.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("self")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/person?page=0&size=10&direction=asc")
            && link.getType().equals("GET")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("create")
            && link.getHref().endsWith("/person")
            && link.getType().equals("POST")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("update")
            && link.getHref().endsWith("/person")
            && link.getType().equals("PUT")
        ));

        assertTrue(result.getLinks().stream().anyMatch(link -> 
            link.getRel().value().equals("delete")
            && link.getHref().endsWith("/person/1")
            && link.getType().equals("DELETE")
        ));

        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getGender(), result.getGender());
    }

    @Test
    void testUpdateWithNullPerson(){
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> {
            services.update(null);
        });

        String expetedMessage = "It is not allowed to persist a null object";
        String actualMessage = ex.getMessage();

        assertTrue(actualMessage.contains(expetedMessage));
    }

    private static void validateIndividualPerson(PersonDto person, int i) {
        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getLinks());

        assertNotNull(person.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/person/" + i)
                        && link.getType().equals("GET")
                ));

        assertNotNull(person.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/person/")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(person.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/person/")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(person.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/person/")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(person.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/person/" + i)
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Address Test " + i, person.getAddress());
        assertEquals("First Name Test " + i, person.getFirstName());
        assertEquals("Last Name Test " + i, person.getLastName());
        assertEquals(((i % 2)==0) ? "Male" : "Female", person.getGender());
    }
}
