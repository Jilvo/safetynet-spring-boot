package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.JsonFileServiceMock;
import com.safetynet.alerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTest extends JsonFileServiceMock {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PersonController(jsonFileService)).build();
    }

    private Person createPerson() {
        Person person = new Person();
        person.setFirstName("Harry");
        person.setLastName("Potter");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-6512");
        person.setEmail("mail@mail.com");
        return person;
    }

    private Person updateOrDeletePerson() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-6512");
        person.setEmail("mail@mail.com");
        return person;
    }

    private byte[] convertObjectToJsonBytes(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(obj);
    }

//    @Test
//    public void testGetPerson() throws Exception {
//        JsonFileServiceMock();
//        ResultActions result = mockMvc.perform(get("/person")
//                .param("stationNumber", "2"
//                )).andExpect(status().isOk());
//    }

    @Test
    public void testCreatePerson() throws Exception {
        JsonFileServiceMock();
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(createPerson()))).andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePerson() throws Exception {
        JsonFileServiceMock();
        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(updateOrDeletePerson()))).andDo(print()).andExpect(status().isAccepted());

    }

    @Test
    public void testDeletePerson() throws Exception {
        JsonFileServiceMock();
        mockMvc.perform(delete("/person").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(updateOrDeletePerson()))).andDo(print()).andExpect(status().isAccepted());

    }


}
