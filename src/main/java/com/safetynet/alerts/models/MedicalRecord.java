package com.safetynet.alerts.models;

import java.util.List;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

public class MedicalRecord {
    public String firstName;
    public String lastName;
    public String birthdate;
    public List<String> medications;
    public List<String> allergies;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public static MedicalRecord fromDict(String jsonString) {
        return JsonIterator.deserialize(jsonString, MedicalRecord.class);
    }

    public static String toDict(MedicalRecord medicalRecord) {
        return JsonStream.serialize(medicalRecord);
    }
}
