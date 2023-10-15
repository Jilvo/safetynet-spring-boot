package com.safetynet.alerts.models;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

public class Person {
    public String firstName;
    public String lastName;
    public String address;
    public String city;
    public String zip;
    public String phone;
    public String email;
    public static Person fromDict(String jsonString){
        return JsonIterator.deserialize(jsonString, Person.class);
    }
    public static String toDict(Person person){
        return JsonStream.serialize(person);
    }

}
