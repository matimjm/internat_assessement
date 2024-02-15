package com.example.internat_assessement;

public class Brand {   // Brand is a model class, which we use for defining the objects of brands,
    // it holds the constructor, getters and setters that are required for a model class to be a model class,
    // very often this class is also used as a type of a data that is wanted in an array,
    // because in our Firestore, we have collections (such as "Brands") which have many documents (a single document is a single brand),
    // each object that is defined in this class (brandId, brandName) is corresponding with each of the fields in a documents in a collection "Brands"

    String brandId, brandName;   // These are the objects that are equivalent for the fields in documents in "Brands" collection

    public Brand(){}   // Empty constructor is needed for a model class to exist

    public Brand(String brandId, String brandName) { // A constructor is needed for a model class to exist
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
