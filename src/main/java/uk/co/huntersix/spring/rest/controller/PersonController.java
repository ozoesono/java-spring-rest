package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.dto.PersonDto;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<?> person(@PathVariable(value="lastName") String lastName,
                                 @PathVariable(value="firstName") String firstName) {

        Optional<Person> person = personDataService.findPerson(lastName, firstName);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/person/{lastName}")
    public ResponseEntity<?> person(@PathVariable(value="lastName") String lastName) {

        List<Person> people = personDataService.findPeopleBySurname(lastName);
        if (people.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(people);
    }

    @PostMapping("/persons")
    public ResponseEntity<?> person(@RequestBody PersonDto personDto) {
        try {
            Person newPerson = personDataService.addPerson(personDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}