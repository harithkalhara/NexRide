package com.nexride.Model;

public class Issue {

    // Instance Variables
    private String userId = "";
    private String category = "Other";
    private String subject = "";
    private String description = "";
    private String status = "OPEN";
    private String reply = "";

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String value) {
        this.category = value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String value) {
        this.subject = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String value) {
        this.reply = value;
    }
}
