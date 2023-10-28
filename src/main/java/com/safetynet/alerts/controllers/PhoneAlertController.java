package com.safetynet.alerts.controllers;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.safetynet.alerts.models.Firestation;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {
    @Autowired
    public PhoneAlertController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    private static final Logger logger = LogManager.getLogger(PhoneAlertController.class);
    private final JsonFileService jsonFileService;

    @GetMapping
    public List<String> getPhoneAlert(@RequestParam(name = "firestation_number") String firestation_number) throws IOException {
        logger.info("Call to /phoneAlert with Method GET");
        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        Any firestationsAny = jsonObject.get("firestations");
        List<String> phoneNumberList = new ArrayList<>();
        List<String> addressForStationNumberList = new ArrayList<>();
        if (firestationsAny != null && firestationsAny.valueType() == ValueType.ARRAY) {
            for (Any firestationItem : firestationsAny) {
                Firestation firestation = Firestation.fromDict(firestationItem.toString());
                if (firestation.getStation().equals(firestation_number)) {
                    addressForStationNumberList.add(firestation.getAddress());
                }
                //            if (firestationItem.get("stationNumber").asText().equals(stationNumber)) {}
            }
        }
        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY) {
            for (String stationNumberAdress : addressForStationNumberList) {
                for (Any personItem : personsAny) {
                    Person person = Person.fromDict(personItem.toString());
                    if (person.getAddress() != null && person.getAddress().equals(stationNumberAdress) && !(phoneNumberList.contains(person.getPhone()))) {
                        phoneNumberList.add(person.getPhone());
                    }
                }

            }
        }
        logger.info("Endpoint returned: " + phoneNumberList);
        return phoneNumberList;
    }
}
