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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/person")
public class PersonController {

//    @Autowired
//    public PersonController(JsonFileService jsonFileService) {
//        this.jsonFileService = jsonFileService;
//    }
    @Autowired
    public JsonFileService jsonFileService;
    private static final Logger logger = LogManager.getLogger(PersonController.class);

    @PostMapping
    public ResponseEntity<String> createPerson(@RequestBody Person newPerson) throws IOException {
        logger.info("Call to /person with Method POST");
        if (newPerson != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode personsNode = (ArrayNode) root.get("persons");
            Map<String, Person> personMap = new HashMap<>();
            for (JsonNode personItem : personsNode) {
                if (newPerson.getFirstName().equals(personItem.get("firstName").asText())
                        && newPerson.getLastName().equals(personItem.get("lastName").asText())) {
                    logger.error("Endpoint returned: Person already exists");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Person already exists");
                }
            }
            JsonNode newPersonNode = objectMapper.valueToTree(newPerson);

            personsNode.add(newPersonNode);

            String createdJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(createdJsonString);
            logger.info("Endpoint returned: " + createdJsonString);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdJsonString);
        } else {
            logger.error("Endpoint returned: Input empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }

    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person toUpdatePerson) throws IOException {
        logger.info("Call to /person with Method PUT");
        if (toUpdatePerson != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode personsNode = (ArrayNode) root.get("persons");
            List<JsonNode> updatedPersonsList = new ArrayList<>();
            Boolean isExists = false;
            for (JsonNode personItem : personsNode) {
                if (toUpdatePerson.getFirstName().equals(personItem.get("firstName").asText()) &&
                        toUpdatePerson.getLastName().equals(personItem.get("lastName").asText())) {
                    JsonNode UpdatePersonNode = objectMapper.valueToTree(toUpdatePerson);
                    updatedPersonsList.add(UpdatePersonNode);
                    isExists = true;
                }
                if (!(toUpdatePerson.getFirstName().equals(personItem.get("firstName").asText()) &&
                        toUpdatePerson.getLastName().equals(personItem.get("lastName").asText()))) {
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
            logger.info("Endpoint returned: " + updatedJsonString);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedJsonString);
        } else {
            logger.error("Endpoint returned: Input empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestBody Person toDeletePerson) throws IOException {
        logger.info("Call to /person with Method DELETE");
        if (toDeletePerson != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode personsNode = (ArrayNode) root.get("persons");
            List<JsonNode> updatedPersonsList = new ArrayList<>();
            Boolean isExists = false;
            for (JsonNode personItem : personsNode) {
                if (toDeletePerson.getFirstName().equals(personItem.get("firstName").asText()) &&
                        toDeletePerson.getLastName().equals(personItem.get("lastName").asText())) {
                    isExists = true;
                }
                if (!(toDeletePerson.getFirstName().equals(personItem.get("firstName").asText()) &&
                        toDeletePerson.getLastName().equals(personItem.get("lastName").asText()))) {
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
            logger.info("Endpoint returned: Deleted");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
        } else {
            logger.error("Endpoint returned: Input empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }
}
