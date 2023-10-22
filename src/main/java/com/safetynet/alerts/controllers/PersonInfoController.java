package com.safetynet.alerts.controllers;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
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
import java.util.Map;

@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {
    private final JsonFileService jsonFileService;

    @Autowired
    public PersonInfoController(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    @GetMapping
    public List<Person> getPersonInfo(@RequestParam(name = "firstname") String firstname, @RequestParam(name = "lastname") String lastname) throws IOException {
        String jsonString  = jsonFileService.readJsonFile();
        Any jsonObject = JsonIterator.deserialize(jsonString);
        Any personsAny = jsonObject.get("persons");
        List<Person> personList = new ArrayList<>();
        if (personsAny != null && personsAny.valueType() == ValueType.ARRAY) {
            for (Any personItem : personsAny) {
                Person person = Person.fromDict(personItem.toString());
                if (person.getFirstName() != null && person.getLastName() != null && person.getFirstName().equals(firstname) && person.getLastName().equals(lastname)){
                    personList.add(person);
                }
            }
        }
        return personList;
    }
}