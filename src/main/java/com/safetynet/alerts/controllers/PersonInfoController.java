package com.safetynet.alerts.controllers;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.safetynet.alerts.models.Person;
import com.safetynet.alerts.services.JsonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {

    @Autowired
    public PersonInfoController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    private static final Logger logger = LogManager.getLogger(PhoneAlertController.class);
    private final JsonFileService jsonFileService;

    @GetMapping
    public List<Person> getPersonInfo(@RequestParam(name = "firstname") String firstname, @RequestParam(name = "lastname") String lastname) throws IOException {
        logger.info("Call to /personInfo with Method GET");
        String jsonString = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        List<Person> personList = new ArrayList<>();
        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY) {
            for (Any personItem : personsAny) {
                Person person = Person.fromDict(personItem.toString());
                if (person.getFirstName() != null && person.getLastName() != null && person.getFirstName().equals(firstname) && person.getLastName().equals(lastname)) {
                    personList.add(person);
                }
            }
        }
        logger.info("Endpoint returned: " + personList);
        return personList;
    }
}