package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.safetynet.alerts.models.MedicalRecord;
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
@RequestMapping("/medicalRecord")
public class MedialRecordController {
    private final JsonFileService jsonFileService;

    @Autowired
    public MedialRecordController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    private static final Logger logger = LogManager.getLogger(MedialRecordController.class);

    @PostMapping
    public ResponseEntity<String> createMedicalRecord(@RequestBody MedicalRecord toCreateMedicalRecord) throws IOException {
        logger.info("Call to /medicalRecord with Method POST");

        if (toCreateMedicalRecord != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode medicalrecordsNode = (ArrayNode) root.get("medicalrecords");
            Map<String, MedicalRecord> personMap = new HashMap<>();
            for (JsonNode personItem : medicalrecordsNode) {
                if (toCreateMedicalRecord.getFirstName().equals(personItem.get("firstName").asText())
                        && toCreateMedicalRecord.getLastName().equals(personItem.get("lastName").asText())) {
                    logger.error("Endpoint returned: MedicalRecord already exists");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("MedicalRecord already exists");
                }
            }
            JsonNode newPersonNode = objectMapper.valueToTree(toCreateMedicalRecord);

            medicalrecordsNode.add(newPersonNode);

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
    public ResponseEntity<String> updateMedicalRecord(@RequestBody MedicalRecord toUpdateMedicalRecord) throws IOException {
        logger.info("Call to /medicalRecord with Method PUT");

        if (toUpdateMedicalRecord != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode medicalrecordsNode = (ArrayNode) root.get("medicalrecords");
            List<JsonNode> updatedMedicalRecords = new ArrayList<>();
            Boolean isExist = false;
            for (JsonNode medicalRecordItem : medicalrecordsNode) {
                if (toUpdateMedicalRecord.getFirstName().equals(medicalRecordItem.get("firstName").asText()) &&
                        toUpdateMedicalRecord.getLastName().equals(medicalRecordItem.get("lastName").asText())) {
                    JsonNode UpdatePersonNode = objectMapper.valueToTree(toUpdateMedicalRecord);
                    updatedMedicalRecords.add(UpdatePersonNode);
                    isExist = true;
                }
                if (!(toUpdateMedicalRecord.getFirstName().equals(medicalRecordItem.get("firstName").asText()) &&
                        toUpdateMedicalRecord.getLastName().equals(medicalRecordItem.get("lastName").asText()))) {
                    updatedMedicalRecords.add(medicalRecordItem);
                }
            }
            if (!isExist) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MedicalRecord does not exists");
            }
            ((ArrayNode) medicalrecordsNode).removeAll();
            ((ArrayNode) medicalrecordsNode).addAll(updatedMedicalRecords);
            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);
            logger.info("Endpoint returned: " + updatedJsonString);

            return ResponseEntity.status(HttpStatus.CREATED).body(updatedJsonString);
        } else {
            logger.error("Endpoint returned: Input empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMedicalRecord(@RequestBody MedicalRecord toDeleteMedicalRecord) throws IOException {
        logger.info("Call to /medicalRecord with Method DELETE");

        if (toDeleteMedicalRecord != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode medicalrecordsNode = (ArrayNode) root.get("medicalrecords");
            List<JsonNode> updatedMedicalRecordsList = new ArrayList<>();
            Boolean isExist = false;
            for (JsonNode personItem : medicalrecordsNode) {
                if (toDeleteMedicalRecord.getFirstName().equals(personItem.get("firstName").asText()) &&
                        toDeleteMedicalRecord.getLastName().equals(personItem.get("lastName").asText())) {
                    isExist = true;
                }
                if (!(toDeleteMedicalRecord.getFirstName().equals(personItem.get("firstName").asText()) &&
                        toDeleteMedicalRecord.getLastName().equals(personItem.get("lastName").asText()))) {
                    updatedMedicalRecordsList.add(personItem);
                }
            }
            if (!isExist) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MedicalRecord does not exists");
            }
            ((ArrayNode) medicalrecordsNode).removeAll();
            ((ArrayNode) medicalrecordsNode).addAll(updatedMedicalRecordsList);
            String updatedJsonString = objectMapper.writeValueAsString(root);
            jsonFileService.writeJsonFile(updatedJsonString);
            logger.info("Endpoint returned: Deleted");

            return ResponseEntity.status(HttpStatus.CREATED).body("Deleted");
        } else {
            logger.error("Endpoint returned: Input empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }
}
