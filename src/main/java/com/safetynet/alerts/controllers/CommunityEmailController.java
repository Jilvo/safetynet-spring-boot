package com.safetynet.alerts.controllers;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.JsonFileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/communityEmail")
public class CommunityEmailController {
    private final JsonFileService jsonFileService;

    @Autowired
    public CommunityEmailController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    private static final Logger logger = LogManager.getLogger(CommunityEmailController.class);

    @GetMapping
    public List<String> getCommunityEmail(@RequestParam(name = "city") String city) throws IOException {
        logger.info("Call to /communityEmail with Method GET");

        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");

        List<String> personList = new ArrayList<>();
        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY) {
            for (Any personItem : personsAny) {
                Person person = Person.fromDict(personItem.toString());
                if (person.getCity() != null && person.getEmail() != null && person.getCity().equals(city)) {
                    personList.add(person.getEmail());
                }
            }
        }
        logger.info("Endpoint returned: " + personList);

        return personList;
    }
}