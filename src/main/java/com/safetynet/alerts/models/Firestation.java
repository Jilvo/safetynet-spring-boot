package com.safetynet.alerts.models;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

public class Firestation {
    public String address;
    public String station;

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getStation() {
        return station;
    }
    public void setStation(String station) {
        this.station = station;
    }
    public static Firestation fromDict(String jsonString) {
        return JsonIterator.deserialize(jsonString, Firestation.class);
    }

    public static String toDict(Firestation firestation) {
        return JsonStream.serialize(firestation);
    }
}

