package com.safetynet.alerts.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
        if (newPerson!= null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode personsNode = (ArrayNode) root.get("persons");
            Map<String, Person> personMap = new HashMap<>();
            for (JsonNode personItem : personsNode) {
                if (newPerson.firstName.equals(personItem.get("firstName").asText())
                        && newPerson.lastName.equals(personItem.get("lastName").asText())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Person already exists");
                }
            }
            JsonNode newPersonNode = objectMapper.valueToTree(newPerson);

            personsNode.add(newPersonNode);

            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }
    @PutMapping
    public ResponseEntity <String> updatePerson(@RequestBody Person toUpdatePerson) throws IOException {
        if (toUpdatePerson!= null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode personsNode = (ArrayNode) root.get("persons");
            List<JsonNode> updatedPersonsList = new ArrayList<>();
            Boolean isExists = false;
            for (JsonNode personItem : personsNode) {
                if (toUpdatePerson.firstName.equals(personItem.get("firstName").asText()) &&
                        toUpdatePerson.lastName.equals(personItem.get("lastName").asText())) {
                    JsonNode UpdatePersonNode = objectMapper.valueToTree(toUpdatePerson);
                    updatedPersonsList.add(UpdatePersonNode);
                    isExists = true;
                }
                if (!(toUpdatePerson.firstName.equals(personItem.get("firstName").asText()) &&
                        toUpdatePerson.lastName.equals(personItem.get("lastName").asText()))) {
                    updatedPersonsList.add(personItem);
                }
            }
            if (!isExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person does not exists");
            }
            ((ArrayNode) personsNode).removeAll();
            ((ArrayNode) personsNode).addAll(updatedPersonsList);
            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }
    @DeleteMapping
    public ResponseEntity <String> deletePerson(@RequestBody Person toDeletePerson) throws IOException {
        if (toDeletePerson!= null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode personsNode = (ArrayNode) root.get("persons");
            List<JsonNode> updatedPersonsList = new ArrayList<>();
            Boolean isExists = false;
            for (JsonNode personItem : personsNode) {
                if (toDeletePerson.firstName.equals(personItem.get("firstName").asText()) &&
                        toDeletePerson.lastName.equals(personItem.get("lastName").asText())) {
                    isExists = true;
                }
                if (!(toDeletePerson.firstName.equals(personItem.get("firstName").asText()) &&
                        toDeletePerson.lastName.equals(personItem.get("lastName").asText()))) {
                    updatedPersonsList.add(personItem);
                }
            }
            if (!isExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person does not exists");
            }
            ((ArrayNode) personsNode).removeAll();
            ((ArrayNode) personsNode).addAll(updatedPersonsList);
            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }
}
