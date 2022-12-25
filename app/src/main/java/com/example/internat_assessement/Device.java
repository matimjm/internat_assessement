package com.example.internat_assessement;

public class Device {

    String modelId, IMEIOrSNum, clientId;

    public Device(){}

    public Device(String modelId, String IMEIOrSNum, String clientId) {
        this.modelId = modelId;
        this.IMEIOrSNum = IMEIOrSNum;
        this.clientId = clientId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getIMEIOrSNum() {
        return IMEIOrSNum;
    }

    public void setIMEIOrSNum(String IMEIOrSNum) {
        this.IMEIOrSNum = IMEIOrSNum;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
