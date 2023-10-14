package com.safetynet.alerts.controllers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/person")
public class PersonController {
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
