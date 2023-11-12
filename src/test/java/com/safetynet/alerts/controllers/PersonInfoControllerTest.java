package com.safetynet.alerts.controllers;

import com.safetynet.alerts.JsonFileServiceMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonInfoController.class)
public class PersonInfoControllerTest extends JsonFileServiceMock {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PersonInfoController(jsonFileService)).build();
    }

    @Test
    public void testGetPersonInfo() throws Exception {
        JsonFileServiceMock();
        ResultActions result = mockMvc.perform(get("/personInfo")
                .param("firstname", "John")
                .param("lastname", "Boyd")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}

