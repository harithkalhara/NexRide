package com.nexride.model;

public class User extends BaseEntity {
    private String name = "";
    private String email = "";
    private String phone = "";
    private String role = "USER";

    public String getName() { return name; }
    public void setName(String value) { name = value == null ? "" : value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value == null ? "" : value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = value == null ? "" : value; }
    public String getRole() { return role; }
    public void setRole(String value) { role = value == null ? "USER" : value; }
}
