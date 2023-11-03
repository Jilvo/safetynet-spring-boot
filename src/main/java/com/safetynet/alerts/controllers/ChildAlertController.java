package com.safetynet.alerts.controllers;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.safetynet.alerts.models.MedicalRecord;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {
    private final JsonFileService jsonFileService;

    @Autowired
    public ChildAlertController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    private static final Logger logger = LogManager.getLogger(ChildAlertController.class);

    @GetMapping
    public List<Map<String, Object>> getChildAlert(@RequestParam(name = "address") String address) throws IOException, ParseException {
        logger.info("Call to /childAlert with Method GET");
        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        Any medicalRecordsAny = jsonObject.get("medicalrecords");
        List<Map<String, Object>> childList = new ArrayList<>();

        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY && medicalRecordsAny != null && medicalRecordsAny.valueType() == ValueType.ARRAY) {
            Map<String, Person> personMap = new HashMap<>();

            for (Any personItem : personsAny) {
                Person person = Person.fromDict(personItem.toString());
                personMap.put(person.getFirstName() + person.getLastName(), person);
            }
            for (Any medicalRecordItem : medicalRecordsAny) {
                MedicalRecord medicalRecord = MedicalRecord.fromDict(medicalRecordItem.toString());

                Person person = personMap.get(medicalRecord.getFirstName() + medicalRecord.getLastName());

                if (person != null && person.getAddress() != null && person.getAddress().equals(address)) {
                    Date birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(medicalRecord.getBirthdate());
                    Date currentDate = new Date();
                    long ageInMillis = currentDate.getTime() - birthdate.getTime();
                    long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);

                    if (ageInYears <= 18) {
                        Map<String, Object> child = new HashMap<>();
                        child.put("firstName", person.getFirstName());
                        child.put("lastName", person.getLastName());
                        child.put("birthdate", medicalRecord.getBirthdate());

                        List<Map<String, String>> otherMembers = new ArrayList<>();
                        for (Any otherPersonItem : personsAny) {
                            Person otherPerson = Person.fromDict(otherPersonItem.toString());
                            if (otherPerson.getAddress().equals(person.getAddress())) {
                                Map<String, String> otherMemberData = new HashMap<>();
                                otherMemberData.put("firstName", otherPerson.getFirstName());
                                otherMemberData.put("lastName", otherPerson.getLastName());
                                otherMemberData.put("birthdate", medicalRecord.getBirthdate());
                                otherMembers.add(otherMemberData);
                            }
                        }
                        child.put("Other Members living in same address", otherMembers);
                        childList.add(child);
                    }
                }
            }
        }
        logger.info("Endpoint returned: " + childList);
        return childList;
    }
}