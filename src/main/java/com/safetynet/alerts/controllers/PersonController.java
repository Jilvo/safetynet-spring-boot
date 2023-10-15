package com.safetynet.alerts.controllers;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jsoniter.any.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.services.JsonFileService;

import java.io.IOException;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
@RestController
@RequestMapping("/person")
public class PersonController {
    private final JsonFileService jsonFileService;

    @Autowired
    public PersonController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }
//endpoint de tests
    @GetMapping
    public String gettest() throws IOException {
        String jsonString  = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        String text = jsonObject.get("persons").toString();
        System.out.println(text);
        return text;
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
