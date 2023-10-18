package com.safetynet.alerts.controllers;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jsoniter.any.Any;
import com.safetynet.alerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.services.JsonFileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @PostMapping
    public ResponseEntity <String>createPerson(@RequestBody Person newPerson) throws IOException {
        System.out.println(newPerson);
        if (newPerson!= null) {
            String jsonString = jsonFileService.readJsonFile();
            Any jsonObject = JsonIterator.deserialize(jsonString);
            Any personsAny = jsonObject.get("persons");
            Map<String, Person> personMap = new HashMap<>();
            for (Any personItem : personsAny) {
                Person person = Person.fromDict(personItem.toString());
                personMap.put(person.firstName + person.lastName, person);
            }
            if (personMap.get(newPerson.firstName + newPerson.lastName) == null){
                personMap.put(newPerson.firstName + newPerson.lastName, newPerson);
            }
            List<String> updatedPersonsList = new ArrayList<>();
            for (Person person : personMap.values()) {
                updatedPersonsList.add(Person.toDict(person));
            }
            jsonObject.set("persons", updatedPersonsList);
//            else{
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person already exists");
//            }
            return ResponseEntity.status(HttpStatus.CREATED).body("ok");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
    }
    }
    @PutMapping
    public String updatePerson(@RequestBody Person person) {
        return "Hello";
    }
    @DeleteMapping
    public String deletePerson(@RequestBody Person person) {
        return "Hello";
    }
}
