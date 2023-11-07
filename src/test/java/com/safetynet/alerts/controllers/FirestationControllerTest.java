package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.JsonFileServiceMock;
import com.safetynet.alerts.models.Firestation;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(FirestationController.class)
public class FirestationControllerTest extends JsonFileServiceMock  {
    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FirestationController(jsonFileService)).build();
    }

    private Firestation createFirestation() {
        Firestation firestation = new Firestation();
        firestation.setAddress("4 Privet Drive");
        firestation.setStation(String.valueOf(1));
        return firestation;
    }
    private Firestation updateOrDeleteFirestation() {
        Firestation firestation = new Firestation();
        firestation.setAddress("112SteppesPl");
        firestation.setStation(String.valueOf(1));
        return firestation;
    }
    private byte[] convertObjectToJsonBytes(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(obj);
    }

    @Test
    public void testGetFirestation() throws Exception {
        JsonFileServiceMock();
        ResultActions result = mockMvc.perform(get("/firestation")
                .param("stationNumber", "2"
        )).andExpect(status().isOk());
    }

    @Test
    public void testCreateFirestation() throws Exception {
        // Simuler le comportement du service JSONFileService
        JsonFileServiceMock();
        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(createFirestation()))).andDo(print()).andExpect(status().isCreated());
    }
    @Test
    public void testUpdateFirestation() throws Exception {
        JsonFileServiceMock();
        mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(updateOrDeleteFirestation()))).andDo(print()).andExpect(status().isAccepted());

    }
    @Test
    public void testDeleteFirestation() throws Exception {
        JsonFileServiceMock();
        mockMvc.perform(delete("/firestation").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(updateOrDeleteFirestation()))).andDo(print()).andExpect(status().isAccepted());

    }
}
