package com.nexride.model;

public class User extends BaseEntity {
  private String name = "";
  private String email = "";
  private String phone = "";
  private String passwordHash = "";
  private String role = "CUSTOMER";
  private String country = "Sri Lanka";
  private String currency = "LKR";
  private String avatarUrl = "";
  private boolean active = true;

  public String getName() {
    return name;
  }

  public void setName(String value) {
    this.name = value;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String value) {
    this.email = value;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String value) {
    this.phone = value;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String value) {
    this.passwordHash = value;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String value) {
    this.role = value;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String value) {
    this.country = value;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String value) {
    this.currency = value;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String value) {
    this.avatarUrl = value;
  }

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean value) {
    this.active = value;
  }
}
