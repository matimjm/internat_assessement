package com.example.internat_assessement;

public class Model {   // Brand is a model class, which we use for defining the objects of models,
    // it holds the constructor, getters and setters that are required for a model class to be a model class,
    // very often this class is also used as a type of a data that is wanted in an array,
    // because in our Firestore, we have collections (such as "Models") which have many documents (a single document is a single model),
    // each object that is defined in this class (brandId, modelId, modelName) is corresponding with each of the fields in a documents in a collection "Models"

    String brandId, modelName, modelId;   // These are the objects that are equivalent for the fields in documents in "Brands" collection

    public Model(){}   // Empty constructor is needed for a model class to exist

    public Model(String brandId, String modelName, String modelId) { // A constructor is needed for a model class to exist
        this.brandId = brandId;
        this.modelName = modelName;
        this.modelId = modelId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
