package com.safetynet.alerts.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {
    @GetMapping
    public String getChildAlert(@RequestParam(name = "address") String address) {
        return "Hello";
    }
}
