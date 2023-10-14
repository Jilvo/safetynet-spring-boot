package com.safetynet.alerts.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/communityEmail")
public class CommunityEmailController {
    @GetMapping
    public String getCommunityEmail(@RequestParam(name = "city") String city) {
        return "Hello";
    }
}