package com.safetynet.alerts.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {
    @GetMapping
    public String getPersonInfo(@RequestParam(name = "firstname") String firstname,@RequestParam(name = "lastname") String lastname)  {
        return "Hello";
    }
}