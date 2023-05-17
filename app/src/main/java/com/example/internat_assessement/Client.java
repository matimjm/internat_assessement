package com.example.internat_assessement;

public class Client {   // Client is a model class, which we use for defining the objects of clients,
                        // it holds the constructor, getters and setters that are required for a model class to be a model class,
                        // very often this class is also used as a type of a data that is wanted in an array,
                        // because in our Firestore, we have collections (such as "Clients") which have many documents (a single document is a single client),
                        // each object that is defined in this class (email, number, clientId) is corresponding with each of the fields in a documents in a collection "Clients"

    String email, number,clientId, name, surname;  // These are the objects that are equivalent for the fields in documents in "Clients" collection

    public Client(){}   // Empty constructor is needed for a model class to exist

    public Client(String email, String number, String clientId, String name, String surname) {   // A constructor is needed for a model class to exist
        this.email = email;
        this.number = number;
        this.clientId = clientId;
        this.name = name;
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {  // The getter of an object email
        return email;
    }

    public void setEmail(String email) {    // The setter of an object email
        this.email = email;
    }

    public String getNumber() { // The getter of an object number
        return number;
    }

    public void setNumber(String number) {  // The setter of an object number
        this.number = number;
    }

    public String getClientId() {   // The getter of an object clientId, it is used in the case of getting the clientId when a user clicked on a client,
                                    // in order to pass the clientId for the Intent in order to later,
                                    // in DeviceAddActivity query for only the devices belonging to the client that the user has chosen
        return clientId;
    }

    public void setClientId(String clientId) {  // The setter of an object clientId
        this.clientId = clientId;
    }
}

