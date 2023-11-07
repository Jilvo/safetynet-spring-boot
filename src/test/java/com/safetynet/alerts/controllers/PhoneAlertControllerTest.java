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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneAlertController.class)
public class PhoneAlertControllerTest extends JsonFileServiceMock {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PhoneAlertController(jsonFileService)).build();
    }

    @Test
    public void testGetPhoneAlert() throws Exception {
        JsonFileServiceMock();
        ResultActions result = mockMvc.perform(get("/phoneAlert")
                .param("firestation_number", "2")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
