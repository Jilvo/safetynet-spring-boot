package com.safetynet.alerts.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.services.JsonFileService;

import java.io.IOException;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final JsonFileService jsonFileService;

    @Autowired
    public PersonController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    @GetMapping
    public String gettest() throws IOException {
        String jsonData  = jsonFileService.readJsonFile();
        System.out.println(jsonData);
        return jsonData;
    }
    @PostMapping
    public String createPerson(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }
    @PutMapping
    public String updatePerson(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }
    @DeleteMapping
    public String deletePerson(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }
}
