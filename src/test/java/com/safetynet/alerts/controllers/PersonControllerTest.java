package com.safetynet.alerts.controllers;

import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.JsonFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonFileService jsonFileService;


    @Test
    public void testCreatePerson() throws Exception {
        Person person1 = new Person();
        person1.setFirstName("Jhn");
        person1.setLastName("BoAd");
        person1.setAddress("1509Culver St");
        person1.setCity("Culvr");
        person1.setZip("9751");
        person1.setPhone("841-74-6512");
        person1.setEmail("jaoyd@email.com");

        mockMvc
                .perform(post("/person").content(String.valueOf(person1)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}


