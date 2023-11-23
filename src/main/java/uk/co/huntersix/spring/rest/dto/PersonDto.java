package uk.co.huntersix.spring.rest.dto;

import uk.co.huntersix.spring.rest.model.Person;

import java.util.Objects;

public class PersonDto {
    private String firstName;
    private String lastName;

    private PersonDto() {
        // empty
    }

    public PersonDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return firstName.equals(personDto.firstName) && lastName.equals(personDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
