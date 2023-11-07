package com.safetynet.alerts.controllers;

import com.safetynet.alerts.JsonFileServiceMock;
import com.safetynet.alerts.services.JsonFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FloodController.class)
public class FloodControllerTest extends JsonFileServiceMock {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FloodController(jsonFileService)).build();
    }

    @Test
    public void testGetFlood() throws Exception {
        JsonFileServiceMock();
        String[] stations = new String[] { "3" };
        ResultActions result = mockMvc.perform(get("/flood")
                .param("stations",stations)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}