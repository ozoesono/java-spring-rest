package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.dto.PersonDto;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDataService {

    protected static final List<Person> PERSON_DATA = new ArrayList<>(Arrays.asList(
            // Wrapped this in new ArrayList to allow modification of list when adding new persons
            new Person("Mary", "Smith"),
            new Person("Brian", "Archer"),
            new Person("Collin", "Brown"),
            new Person("Ray", "Brown"))
    );

    public Optional<Person> findPerson(String lastName, String firstName) {
        return PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public List<Person> findPeopleBySurname(String surname) {
        return PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(surname)).collect(Collectors.toList());
    }

    public Person addPerson(PersonDto personDto) throws Exception {

        Optional<Person> existingPerson = PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(personDto.getFirstName())
                        && p.getLastName().equalsIgnoreCase(personDto.getLastName()))
                .findFirst();

        if (existingPerson.isPresent()) {
            // Just for simplicity, in a real world app this will be a Custom Exception
            throw new Exception("Person already exists");
        }

        // Doing this because the new person needs an id. Person passed above is just used as a DTO
        Person personToAdd = new Person(personDto.getFirstName(), personDto.getLastName());

        PERSON_DATA.add(personToAdd);

        return personToAdd;
    }
}
