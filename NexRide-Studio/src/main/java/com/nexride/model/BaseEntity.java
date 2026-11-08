package com.nexride.model;

import java.util.UUID;

public class BaseEntity {
    private String id = UUID.randomUUID().toString();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
