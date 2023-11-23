package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.dto.PersonDto;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(Optional.of(new Person("Mary", "Smith")));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNoPersonFoundFromService() throws Exception {
        // Non-existence person Louis Martha
        when(personDataService.findPerson("louis", "martha"))
                .thenReturn(Optional.empty());
        this.mockMvc.perform(get("/person/louis/martha"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnOnePersonWithSurnameFromService() throws Exception {
        when(personDataService.findPeopleBySurname("smith"))
                .thenReturn(Collections.singletonList(new Person("Mary", "Smith")));
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Mary"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnTwoPeopleWithSurnameFromService() throws Exception {
        when(personDataService.findPeopleBySurname("brown"))
                .thenReturn(Arrays.asList(new Person("Ray", "Brown"),  new Person("Collin", "Brown")));
        this.mockMvc.perform(get("/person/brown"))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Ray"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].firstName").value("Collin"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotReturnAnyPersonWithSurnameFromService() throws Exception {
        when(personDataService.findPeopleBySurname("non-existent-surname"))
                .thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/person/non-existent-surname"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddNewPerson() throws Exception {

        PersonDto personDto = new PersonDto("Harry", "Bow");
        ObjectMapper objectMapper = new ObjectMapper();
        String personJson = objectMapper.writeValueAsString(personDto);

        when(personDataService.addPerson(personDto)).thenReturn(new Person("Harry", "Bow"));

        this.mockMvc.perform(post("/persons")
                        .contentType("application/json")
                        .content(personJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("firstName").value("Harry"))
                .andExpect(jsonPath("lastName").value("Bow"));
    }

    @Test
    public void shouldNotBeAbleToAddExistingPerson() throws Exception {

        PersonDto personDto = new PersonDto("Mary", "Smith");
        ObjectMapper objectMapper = new ObjectMapper();
        String personJson = objectMapper.writeValueAsString(personDto);

        doThrow(new Exception("Person already exists")).when(personDataService).addPerson(new PersonDto("Mary", "Smith"));

        this.mockMvc.perform(post("/persons")
                .contentType("application/json")
                .content(personJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}