package com.nexride.model;

public class BaseEntity {    //base parent class()
    private String id = java.util.UUID.randomUUID().toString();    //(UUID.randomUUID())generates a unique random ID
    private String createdAt = java.time.Instant.now().toString();    //(Instant.now()) Gets the current date and time.
    

    public String getId() {
        return id;
    }

    public void setId(String value) { //setter used to change the values in the variable
        this.id = value;                //refers to the object's ID variable
    }

    public String getCreatedAt() {
        return createdAt;        //Returns the date and time when the object was created
    }

    public void setCreatedAt(String value) {
        this.createdAt = value;
    }
}
//Encapsulation – Keeping variables private and accessing them through getters and setters
