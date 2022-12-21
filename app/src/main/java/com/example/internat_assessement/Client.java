package com.example.internat_assessement;

public class Client {

    String email, number,clientId;

    public Client(){}

    public Client(String email, String number, String clientId) {
        this.email = email;
        this.number = number;
        this.clientId = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
