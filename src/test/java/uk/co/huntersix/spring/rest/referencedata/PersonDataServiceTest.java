package uk.co.huntersix.spring.rest.referencedata;

import junit.framework.TestCase;
import org.junit.Test;
import uk.co.huntersix.spring.rest.dto.PersonDto;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.List;
import java.util.Optional;

public class PersonDataServiceTest extends TestCase {

    private final PersonDataService personDataService = new PersonDataService();

    @Test
    public void testShouldFindPersonByFirstAndLastName() {
        Optional<Person> result = personDataService.findPerson("Smith", "Mary");
        assertTrue(result.isPresent());
        assertEquals("Mary", result.get().getFirstName());
        assertEquals("Smith", result.get().getLastName());
    }

    @Test
    public void testShouldNotFindPersonWithNonExistingName() {
        Optional<Person> result = personDataService.findPerson("NonExisting", "Name");
        assertFalse(result.isPresent());
    }

    @Test
    public void testShouldFindPeopleBySurname() {
        List<Person> results = personDataService.findPeopleBySurname("Brown");
        assertEquals(2, results.size());
    }

    @Test
    public void testShouldFindNoPeopleWithNonExistingSurname() {
        List<Person> results = personDataService.findPeopleBySurname("NonExisting");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testShouldAddNewPerson() throws Exception {
        PersonDto newPersonDto = new PersonDto("New", "Person");
        Person newPerson = personDataService.addPerson(newPersonDto);
        assertEquals("New", newPerson.getFirstName());
        assertEquals("Person", newPerson.getLastName());
    }
}
