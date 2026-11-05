package com.nexride.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "kind")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Motorcycle.class, name = "MOTORCYCLE"),
  @JsonSubTypes.Type(value = Bicycle.class, name = "BICYCLE")
})
public abstract class Vehicle extends BaseEntity {
  private String name = "";
  private String location = "Colombo";
  private int dailyRate = 0;
  private String photoUrl = "";
  private String photoCredit = "";
  private boolean samplePhoto = false;
  private String status = "AVAILABLE";
  private String description = "";

  public Vehicle() {}

  public abstract String getKind();

  public String getName() {
    return name;
  }

  public void setName(String value) {
    this.name = value;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String value) {
    this.location = value;
  }

  public int getDailyRate() {
    return dailyRate;
  }

  public void setDailyRate(int value) {
    this.dailyRate = value;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String value) {
    this.photoUrl = value;
  }

  public String getPhotoCredit() {
    return photoCredit;
  }

  public void setPhotoCredit(String value) {
    this.photoCredit = value;
  }

  public boolean getSamplePhoto() {
    return samplePhoto;
  }

  public void setSamplePhoto(boolean value) {
    this.samplePhoto = value;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String value) {
    this.status = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String value) {
    this.description = value;
  }
}
