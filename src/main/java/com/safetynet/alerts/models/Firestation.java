package com.safetynet.alerts.models;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

public class Firestation {
    private String address;
    private String station;

    public void fromDict(String jsonString){
        JsonIterator.deserialize(jsonString, Firestation.class);
    }
    public void toDict(){
        System.out.println(JsonStream.serialize(this));
    }
}

