package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/firestation")
public class FirestationController {

    @GetMapping
    public String getFirestation(@RequestParam(name = "stationNumber") String stationNumber) {
        return "Hello";
    }

    private final JsonFileService jsonFileService;

    @Autowired
    public FirestationController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }
    @PostMapping
    public ResponseEntity<String> createFirestation(@RequestBody Firestation newFirestation) throws IOException {
        if (newFirestation!= null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode firestationsNode = (ArrayNode) root.get("firestations");
            Map<String, Firestation> firestationMap = new HashMap<>();
            for (JsonNode firestationItem : firestationsNode) {
                if (newFirestation.address.equals(firestationItem.get("address").asText())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Firestation already exists");
                }
            }
            JsonNode newFirestationNode = objectMapper.valueToTree(newFirestation);

            firestationsNode.add(newFirestationNode);

            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }

    @PutMapping
    public ResponseEntity <String> updateFirestation(@RequestBody Firestation toUpdateFirestation) throws IOException {
        if (toUpdateFirestation!= null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode firestationsNode = (ArrayNode) root.get("firestations");
            List<JsonNode> updatedFirestationsList = new ArrayList<>();
            Boolean isExist = false;
            for (JsonNode firestationItem : firestationsNode) {
                if (toUpdateFirestation.address.equals(firestationItem.get("address").asText())) {
                    JsonNode UpdateFirestationsNode = objectMapper.valueToTree(toUpdateFirestation);
                    updatedFirestationsList.add(UpdateFirestationsNode);
                    isExist = true;
                }
                if (!(toUpdateFirestation.address.equals(firestationItem.get("address").asText()))) {
                    updatedFirestationsList.add(firestationItem);
                }
            }
            if (!isExist) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firestation does not exists");
            }
            ((ArrayNode) firestationsNode).removeAll();
            ((ArrayNode) firestationsNode).addAll(updatedFirestationsList);
            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }

    @DeleteMapping
    public ResponseEntity <String> deleteFirestation(@RequestBody Firestation toDeleteFirestation) throws IOException {
        if (toDeleteFirestation!= null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode firestationsNode = (ArrayNode) root.get("firestations");
            List<JsonNode> updatedFirestationsList = new ArrayList<>();
            Boolean isExist = false;
            for (JsonNode firestationItem : firestationsNode) {
                if (toDeleteFirestation.address.equals(firestationItem.get("address").asText()) ) {
                    isExist = true;
                }
                if (!(toDeleteFirestation.address.equals(firestationItem.get("address").asText()))) {
                    updatedFirestationsList.add(firestationItem);
                }
            }
            if (!isExist) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firestation does not exists");
            }
            ((ArrayNode) firestationsNode).removeAll();
            ((ArrayNode) firestationsNode).addAll(updatedFirestationsList);
            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }
}
