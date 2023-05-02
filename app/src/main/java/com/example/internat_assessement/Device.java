package com.example.internat_assessement;

public class Device {   // Device is a model class, which we use for defining the objects of devices,
                        // it holds the constructor, getters and setters that are required for a model class to be a model class,
                        // very often this class is also used as a type of a data that is wanted in an array,
                        // because in our Firestore, we have collections (such as "Devices") which have many documents (a single document is a single device),
                        // each object that is defined in this class (modelId, IMEIOrSNum, clientId) is corresponding with each of the fields in a documents in a collection "Devices"

    String modelId, IMEIOrSNum, clientId;   // These are the objects that are equivalent for the fields in documents in "Devices" collection

    public Device(){}   // Empty constructor is needed for a model class to exist

    public Device(String modelId, String IMEIOrSNum, String clientId) { // A constructor is needed for a model class to exist
        this.modelId = modelId;
        this.IMEIOrSNum = IMEIOrSNum;
        this.clientId = clientId;
    }

    public String getModelId() {    // The getter of an object modelId
        return modelId;
    }

    public void setModelId(String modelId) {    // The setter of an object modelId
        this.modelId = modelId;
    }

    public String getIMEIOrSNum() { // The getter of an object IMEIOrSNum
        return IMEIOrSNum;
    }

    public void setIMEIOrSNum(String IMEIOrSNum) {  // The setter of an object IMEIOrSNum
        this.IMEIOrSNum = IMEIOrSNum;
    }

    public String getClientId() {   // The getter of an object clientId
        return clientId;
    }

    public void setClientId(String clientId) {  // The setter of an object clientId
        this.clientId = clientId;
    }
}
