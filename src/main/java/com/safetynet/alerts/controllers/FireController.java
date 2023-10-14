package com.safetynet.alerts.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/fire")
public class FireController {
    @GetMapping
    public String getFire(@RequestParam(name = "adress") String adress) {
        return "Hello";
    }
}