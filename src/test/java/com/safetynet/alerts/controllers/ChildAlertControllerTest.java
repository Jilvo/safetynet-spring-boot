package com.safetynet.alerts.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(ChildAlertController.class)
public class ChildAlertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildAlertController childAlertController;

    @Test
    public void getChildAlertTest() throws Exception {
        mockMvc.perform(get("/childAlert").param("address", anyString())).andDo(print()).andExpect(status().isOk());
        verify(childAlertController).getChildAlert(anyString());
    }
}
