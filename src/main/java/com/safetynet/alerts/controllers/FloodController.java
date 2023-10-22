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
@RequestMapping("/flood")
public class FloodController {
    private final JsonFileService jsonFileService;

    @Autowired
    public FloodController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    @GetMapping
    public List<HashMap> getFlood(@RequestParam(name = "stations") List<String> stations) throws IOException, ParseException {

        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        Any medicalRecordsAny = jsonObject.get("medicalrecords");
        Any firestationsAny = jsonObject.get("firestations");

        List<HashMap> ListOfAdressInListofStation = new ArrayList<>();
        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY &&
                medicalRecordsAny != null && medicalRecordsAny.valueType() == ValueType.ARRAY &&
                firestationsAny != null && firestationsAny.valueType() == ValueType.ARRAY
        ) {
            Map<String, MedicalRecord> medicalRecordMap = new HashMap<>();

            for (Any personItem : medicalRecordsAny) {
                MedicalRecord medicalRecord = MedicalRecord.fromDict(personItem.toString());
                medicalRecordMap.put(medicalRecord.getFirstName() + medicalRecord.getLastName(), medicalRecord);
            }

//            LIST OF STATION
            for (String station : stations) {
                HashMap<String,Object> ListOfAdressInStation = new HashMap<>();
                List<String> addressForStationNumberList = new ArrayList<>();
                for (Any firestationItem : firestationsAny) {
                    Firestation firestation = Firestation.fromDict(firestationItem.toString());
                    if (firestation.getStation().equals(station)) {
                        addressForStationNumberList.add(firestation.getAddress());
                    }
                }
//                ONE ADDRESS
                HashMap<String,Object> PersonByAdress = new HashMap<>();
                for (String addressForStationNumber : addressForStationNumberList)
                {
                    List<Object> ListPersonByAdress = new ArrayList<>();
//                    ONE PERSON
                    for (Any personItem : personsAny) {
                        Person person = Person.fromDict(personItem.toString());
                        MedicalRecord medicalRecord = medicalRecordMap.get(person.getFirstName() + person.getLastName());
                        if (person.getAddress() != null && person.getAddress().equals(addressForStationNumber)) {
                            HashMap<String, String> personData = new HashMap<>();
                            Date birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(medicalRecord.getBirthdate());
                            Date currentDate = new Date();
                            long ageInMillis = currentDate.getTime() - birthdate.getTime();
                            long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);
                            personData.put("firstName", String.valueOf(person.getFirstName()));
                            personData.put("lastName", String.valueOf(person.getLastName()));
                            personData.put("phonenumber", String.valueOf(person.getPhone()));
                            personData.put("age", String.valueOf(ageInYears));
                            personData.put("medications", String.valueOf(medicalRecord.getMedications()));
                            personData.put("allergies", String.valueOf(medicalRecord.getAllergies()));
                            ListPersonByAdress.add(personData);
                        }
                        PersonByAdress.put("Adress "+addressForStationNumber,ListPersonByAdress);
                    }
                    ListOfAdressInStation.put("Station n°"+station,PersonByAdress);
                }
                ListOfAdressInListofStation.add(ListOfAdressInStation);
            }
        }
        return ListOfAdressInListofStation;
    }


}