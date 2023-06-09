package com.example.todolistrest.controllers;

import com.example.todolistrest.dto.PersonDTO;
import com.example.todolistrest.services.PeopleService;
import com.example.todolistrest.utils.CreateException;
import com.example.todolistrest.utils.NotFoundException;
import com.example.todolistrest.utils.PersonErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PeopleController {
    private final PeopleService peopleService;

    @GetMapping
    public List<PersonDTO> getPeople() {
        return peopleService
                .getPeople()
                .stream()
                .map(peopleService::convertToPersonDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PersonDTO getPersonById(@PathVariable int id) {
        return peopleService.convertToPersonDTO(peopleService.getPerson(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addPerson(@RequestBody PersonDTO personDTO) {
        peopleService.addPerson(personDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(CreateException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(NotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }
}
