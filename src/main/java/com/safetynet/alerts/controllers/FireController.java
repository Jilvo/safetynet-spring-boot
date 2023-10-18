package com.safetynet.alerts.controllers;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.MedicalRecord;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/fire")
public class FireController {
    private final JsonFileService jsonFileService;
    @Autowired
    public FireController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    @GetMapping
    public List<Map<String, String>> getFire(@RequestParam(name = "adress") String adress) throws IOException, ParseException {

        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        Any medicalRecordsAny = jsonObject.get("medicalrecords");
        Any fireStationsAny = jsonObject.get("firestations");
        List<Map<String, String>> personDataList = new ArrayList<>();
        if (personsAny!= null && personsAny.valueType() == ValueType.ARRAY && medicalRecordsAny!= null && medicalRecordsAny.valueType() == ValueType.ARRAY && fireStationsAny!= null) {
        Map<String, Person> personMap = new HashMap<>();
        for (Any personItem : personsAny) {
            Person person = Person.fromDict(personItem.toString());
            personMap.put(person.firstName + person.lastName, person);
        }
        for (Any medicalRecordItem : medicalRecordsAny) {
            MedicalRecord medicalRecord = MedicalRecord.fromDict(medicalRecordItem.toString());
            Person person = personMap.get(medicalRecord.firstName + medicalRecord.lastName);
            if (person!= null && person.address!= null && person.address.equals(adress)) {
                Date birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(medicalRecord.birthdate);
                Date currentDate = new Date();
                long ageInMillis = currentDate.getTime() - birthdate.getTime();
                long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);
                Map<String,String> personData = new HashMap<>();
                personData.put("firstName", person.firstName);
                personData.put("lastName", person.lastName);
                personData.put("age", String.valueOf(ageInYears));
                personData.put("medications", medicalRecord.medications.toString());
                personData.put("allergies", medicalRecord.allergies.toString());
                for (Any fireStationItem : fireStationsAny) {
                    Firestation firestation = Firestation.fromDict(fireStationItem.toString());
                    if(firestation.address.equals(person.address)) {
                        personData.put("firestation", firestation.station);
                    }
                }
                personDataList.add(personData);
            }
        }
        }
        return personDataList;
    }

    }
