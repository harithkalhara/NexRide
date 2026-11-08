package com.nexride.Model;

public class Review extends BaseEntity {

    // Instance Variables
    private String userId = "";
    private String vehicleId = "";
    private int rating = 5;
    private String comment = "";

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String value) {
        this.vehicleId = value;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int value) {
        this.rating = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }
}

