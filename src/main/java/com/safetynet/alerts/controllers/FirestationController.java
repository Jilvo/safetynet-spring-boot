package com.safetynet.alerts.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/firestation")
public class FirestationController {

    @GetMapping
    public String getFirestation(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }

    @PostMapping
    public String createFirestation(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }

    @PutMapping
    public String updateFirestation(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }

    @DeleteMapping
    public String deleteFirestation(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }
}
