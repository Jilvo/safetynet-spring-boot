package com.safetynet.alerts.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/flood")
public class FloodController {
    @GetMapping
    public String getFlood(@RequestParam(name = "stations") List<String> stations) {
        return "Hello";
    }
}