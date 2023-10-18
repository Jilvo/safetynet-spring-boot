package com.safetynet.alerts.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {
    @GetMapping
    public String getPhoneAlert(@RequestParam(name = "firestation") String firestation) {
        return "Hello";
    }
}