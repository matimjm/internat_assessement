package com.example.internat_assessement;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

public class Service {

    String IMEIOrSNum, status, shortInfo, serviceId, longInfo, date;
    Timestamp timestamp;

    public Service(){}

    public Service(String IMEIOrSNum, String status, String shortInfo, String serviceId, String date, String longInfo, Timestamp timestamp) {
        this.IMEIOrSNum = IMEIOrSNum;
        this.status = status;
        this.shortInfo = shortInfo;
        this.serviceId = serviceId;
        this.date = date;
        this.longInfo = longInfo;
        this.timestamp = timestamp;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getShortInfo() {
        return shortInfo;
    }

    public void setShortInfo(String shortInfo) {
        this.shortInfo = shortInfo;
    }

    public String getIMEIOrSNum() {
        return IMEIOrSNum;
    }

    public void setIMEIOrSNum(String IMEIOrSNum) {
        this.IMEIOrSNum = IMEIOrSNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLongInfo() {
        return longInfo;
    }

    public void setLongInfo(String longInfo) {
        this.longInfo = longInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
