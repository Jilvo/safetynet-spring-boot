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


    public static MedicalRecord fromDict(String jsonString){
        return JsonIterator.deserialize(jsonString, MedicalRecord.class);
    }
    public static String toDict(MedicalRecord medicalRecord){
        return JsonStream.serialize(medicalRecord);
    }
}
