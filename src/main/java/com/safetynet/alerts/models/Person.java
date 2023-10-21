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

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
    public static Person fromDict(String jsonString){
        return JsonIterator.deserialize(jsonString, Person.class);
    }
    public static String toDict(Person person){
        return JsonStream.serialize(person);
    }

}
