package com.example.internat_assessement;

import com.google.firebase.Timestamp;

public class Service {  // Service is a model class, which we use for defining the objects of devices,
                        // it holds the constructor, getters and setters that are required for a model class to be a model class,
                        // very often this class is also used as a type of a data that is wanted in an array,
                        // because in our Firestore, we have collections (such as "Services") which have many documents (a single document is a single service),
                        // each object that is defined in this class (IMEIOrSNum, status, shortInfo, serviceId, month_year, date, longInfo, timestamp) is corresponding with each of the fields in a documents in a collection "Services"

    String IMEIOrSNum, status, shortInfo, serviceId, longInfo, month_year, date;        // These are the objects that are equivalent for the fields in documents in "Services" collection
    Timestamp timestamp;                                                                //

    public Service(){}  // Empty constructor is needed for a model class to exist

    public Service(String IMEIOrSNum, String status, String shortInfo, String serviceId, String month_year, String date, String longInfo, Timestamp timestamp) {    // A constructor is needed for a model class to exist
        this.IMEIOrSNum = IMEIOrSNum;
        this.status = status;
        this.shortInfo = shortInfo;
        this.serviceId = serviceId;
        this.month_year = month_year;
        this.date = date;
        this.longInfo = longInfo;
        this.timestamp = timestamp;
    }

    public String getServiceId() {  // The getter of an object serviceId
        return serviceId;
    }

    public void setServiceId(String serviceId) {    // The setter of an object serviceId
        this.serviceId = serviceId;
    }

    public String getShortInfo() {  // The getter of an object shortInfo
        return shortInfo;
    }

    public void setShortInfo(String shortInfo) {    // The setter of an object shortInfo
        this.shortInfo = shortInfo;
    }

    public String getIMEIOrSNum() { // The getter of an object IMEIOrSNum
        return IMEIOrSNum;
    }

    public void setIMEIOrSNum(String IMEIOrSNum) {  // The setter of an object IMEIOrSNum
        this.IMEIOrSNum = IMEIOrSNum;
    }

    public String getStatus() { // The getter of an object status
        return status;
    }

    public void setStatus(String status) {  // The setter of an object status
        this.status = status;
    }

    public String getLongInfo() {   // The getter of an object longInfo
        return longInfo;
    }

    public void setLongInfo(String longInfo) {  // The setter of an object longInfo
        this.longInfo = longInfo;
    }

    public String getMonth_year() { // The getter of an object month_year
        return month_year;
    }

    public void setMonth_year(String month_year) {  // The setter of an object month_year
        this.month_year = month_year;
    }

    public Timestamp getTimestamp() {   // The getter of an object timestamp
        return timestamp;
    }

    public String getDate() {   // The getter of an object date
        return date;
    }

    public void setDate(String date) {  // The setter of an object date
        this.date = date;
    }

    public void setTimestamp(Timestamp timestamp) { // The setter of an object timestamp
        this.timestamp = timestamp;
    }
}
