package com.safetynet.alerts.controllers;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
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
@RequestMapping("/flood")
public class FloodController {
    private final JsonFileService jsonFileService;

    @Autowired
    public FloodController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    @GetMapping
    public List<List> getFlood(@RequestParam(name = "stations") List<String> stations) throws IOException, ParseException {

        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        Any medicalRecordsAny = jsonObject.get("medicalrecords");
        Any fireStationsAny = jsonObject.get("firestations");
        List<Map<String, Object>> PersonDataList = new ArrayList<>();
        List<List> ListOfPersonByStation = null;
        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY &&
                medicalRecordsAny != null && medicalRecordsAny.valueType() == ValueType.ARRAY &&
                fireStationsAny != null && fireStationsAny.valueType() == ValueType.ARRAY
        ) {
            Map<String, Person> personMap = new HashMap<>();
            Map<String, MedicalRecord> medicalRecordMap = new HashMap<>();
//            for (Any personItem : personsAny) {
//                Person person = Person.fromDict(personItem.toString());
//                personMap.put(person.firstName + person.lastName, person);
//            }
            for (Any personItem : medicalRecordsAny) {
                MedicalRecord medicalRecord = MedicalRecord.fromDict(personItem.toString());
                medicalRecordMap.put(medicalRecord.firstName + medicalRecord.lastName, medicalRecord);
            }
//            #TODO il faut faire une liste des adresse sur le numéro de station puis boucler sur la liste en question

            for (String station : stations) {
                for (Any personItem : personsAny) {
                    Person person = Person.fromDict(personItem.toString());
                    List<Person> personDataLivingAtAdress = new ArrayList<>();
                    if (person.address != null && person.address.equals(station)) {
                        Map<String, String> personData = new HashMap<>();
                        MedicalRecord medicalRecordDict = medicalRecordMap.get(person.firstName + person.lastName);
                        MedicalRecord medicalRecord = MedicalRecord.fromDict(medicalRecordDict.toString());
                        Date birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(medicalRecord.birthdate);
                        Date currentDate = new Date();
                        long ageInMillis = currentDate.getTime() - birthdate.getTime();
                        long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);
                        personData.put("firstName", person.firstName);
                        personData.put("lastName", person.lastName);
                        personData.put("age", String.valueOf(ageInYears));
                        personData.put("medications", medicalRecord.medications.toString());
                        personData.put("allergies", medicalRecord.allergies.toString());
                        personDataLivingAtAdress.add((Person) personData);
                    }

                    ListOfPersonByStation.add(personDataLivingAtAdress);
                }

            }

        }
        return ListOfPersonByStation;
    }


}