package com.nexride.model;

public class BaseEntity {
    private String id = java.util.UUID.randomUUID().toString();
    private String createdAt = java.time.Instant.now().toString();

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String value) {
        this.createdAt = value;
    }
}
