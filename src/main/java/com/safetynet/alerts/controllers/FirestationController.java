package com.safetynet.alerts.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.MedicalRecord;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    @Autowired
    public FirestationController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    private final JsonFileService jsonFileService;
    private static final Logger logger = LogManager.getLogger(FirestationController.class);

    @GetMapping
    public Map<String, Object> getFirestation(@RequestParam(name = "stationNumber") String stationNumber) throws IOException, ParseException {
        logger.info("Call to /firestation with Method GET");
        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        Any firestationsAny = jsonObject.get("firestations");
        Any medicalRecordsAny = jsonObject.get("medicalrecords");
        List<String> addressForStationNumberList = new ArrayList<>();
        Map<String, Object> personsForStationNumber = new HashMap<>();
        List<Person> personCoverByStationNumber = new ArrayList<>();
        if (firestationsAny != null && firestationsAny.valueType() == ValueType.ARRAY) {
            for (Any firestationItem : firestationsAny) {
                Firestation firestation = Firestation.fromDict(firestationItem.toString());
                if (firestation.getStation().equals(stationNumber)) {
                    addressForStationNumberList.add(firestation.getAddress());
                }
                //            if (firestationItem.get("stationNumber").asText().equals(stationNumber)) {}
            }
        }
        Map<String, MedicalRecord> medicalRecordsMap = new HashMap<>();
        for (Any personItem : medicalRecordsAny) {
            MedicalRecord medicalRecord = MedicalRecord.fromDict(personItem.toString());
            medicalRecordsMap.put(medicalRecord.getFirstName() + medicalRecord.getLastName(), medicalRecord);
        }
        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY) {
            Integer minorCount = 0;
            Integer adultCount = 0;
            for (String stationNumberAdress : addressForStationNumberList) {
                for (Any personItem : personsAny) {
                    Person person = Person.fromDict(personItem.toString());
                    MedicalRecord medicalRecord = medicalRecordsMap.get(person.getFirstName() + person.getLastName());
                    if (person.getAddress() != null && person.getAddress().equals(stationNumberAdress)) {
                        Date birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(medicalRecord.getBirthdate());
                        Date currentDate = new Date();
                        long ageInMillis = currentDate.getTime() - birthdate.getTime();
                        long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);
                        if (ageInYears <= 18) {
                            minorCount++;
                        } else {
                            adultCount++;

                        }
                        personCoverByStationNumber.add(person);

                    }
                }
                personsForStationNumber.put("List of persons", personCoverByStationNumber);
                personsForStationNumber.put("Number of Childs", minorCount);
                personsForStationNumber.put("Number of Adults", adultCount);
            }
        }
        logger.info("Endpoint returned: " + personsForStationNumber);
        return (Map<String, Object>) personsForStationNumber;
    }

    @PostMapping
    public ResponseEntity<String> createFirestation(@RequestBody Firestation newFirestation) throws IOException {
        logger.info("Call to /firestation with Method POST");

        if (newFirestation != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode firestationsNode = (ArrayNode) root.get("firestations");
            Map<String, Firestation> firestationMap = new HashMap<>();
            for (JsonNode firestationItem : firestationsNode) {
                if (newFirestation.getAddress().equals(firestationItem.get("address").asText())) {
                    logger.error("Endpoint returned: Firestation already exists");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Firestation already exists");
                }
            }
            JsonNode newFirestationNode = objectMapper.valueToTree(newFirestation);

            firestationsNode.add(newFirestationNode);

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
    public ResponseEntity<String> updateFirestation(@RequestBody Firestation toUpdateFirestation) throws IOException {
        logger.info("Call to /firestation with Method PUT");

        if (toUpdateFirestation != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode firestationsNode = (ArrayNode) root.get("firestations");
            List<JsonNode> updatedFirestationsList = new ArrayList<>();
            Boolean isExist = false;
            for (JsonNode firestationItem : firestationsNode) {
                if (toUpdateFirestation.getAddress().equals(firestationItem.get("address").asText())) {
                    JsonNode UpdateFirestationsNode = objectMapper.valueToTree(toUpdateFirestation);
                    updatedFirestationsList.add(UpdateFirestationsNode);
                    isExist = true;
                }
                if (!(toUpdateFirestation.getAddress().equals(firestationItem.get("address").asText()))) {
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
            logger.info("Endpoint returned: " + updatedJsonString);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedJsonString);
        } else {
            logger.error("Endpoint returned: Input empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFirestation(@RequestBody Firestation toDeleteFirestation) throws IOException {
        logger.info("Call to /firestation with Method DELETE");

        if (toDeleteFirestation != null) {
            String jsonString = jsonFileService.readJsonFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            ArrayNode firestationsNode = (ArrayNode) root.get("firestations");
            List<JsonNode> updatedFirestationsList = new ArrayList<>();
            Boolean isExist = false;
            for (JsonNode firestationItem : firestationsNode) {
                if (toDeleteFirestation.getAddress().equals(firestationItem.get("address").asText())) {
                    isExist = true;
                }
                if (!(toDeleteFirestation.getAddress().equals(firestationItem.get("address").asText()))) {
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
            logger.info("Endpoint returned: " + "Deleted");

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted");
        } else {
            logger.error("Endpoint returned: Input empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input empty");
        }
    }
}
