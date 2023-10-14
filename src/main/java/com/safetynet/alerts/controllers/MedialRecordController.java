package com.safetynet.alerts.controllers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/medicalRecord")
public class MedialRecordController {
    @PostMapping
    public String createMedicalRecord(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }
    @PutMapping
    public String updateMedicalRecord(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }
    @DeleteMapping
    public String deleteMedicalRecord(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }
}
