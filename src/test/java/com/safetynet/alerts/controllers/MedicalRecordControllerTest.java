package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.JsonFileServiceMock;
import com.safetynet.alerts.models.MedicalRecord;
import com.safetynet.alerts.services.JsonFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class MedicalRecordControllerTest  extends JsonFileServiceMock {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MedicalRecordController(jsonFileService)).build();
    }

    private MedicalRecord createMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Harry");
        medicalRecord.setLastName("Potter");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of(new String[]{"aznol:350mg", "hydrapermazol:100mg"}));
        medicalRecord.setAllergies(List.of(new String[]{"nillacilan"}));
        return medicalRecord;
    }
    private MedicalRecord updateOrDeletePerson() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("11/07/2024");
        medicalRecord.setMedications(List.of(new String[]{"aznol:350mg", "hydrapermazol:100mg"}));
        medicalRecord.setAllergies(List.of(new String[]{"nillacilan"}));
        return medicalRecord;
    }

    private byte[] convertObjectToJsonBytes(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(obj);
    }
    @Test
    public void testCreateMedicalRecord() throws Exception {
        // Simuler le comportement du service JSONFileService
        JsonFileServiceMock();
         mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(createMedicalRecord()))).andDo(print()).andExpect(status().isCreated());
    }
    @Test
    public void testUpdateMedicalRecord() throws Exception {
        JsonFileServiceMock();
         mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(updateOrDeletePerson()))).andDo(print()).andExpect(status().isAccepted());

    }
    @Test
    public void testDeleteMedicalRecord() throws Exception {
        JsonFileServiceMock();
         mockMvc.perform(delete("/medicalRecord").contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(updateOrDeletePerson()))).andDo(print()).andExpect(status().isAccepted());

    }
}
