package com.nexride.model;

public class RideOffer extends BaseEntity {
  private String hostId = "";
  private String origin = "";
  private String destination = "";
  private String departure = "";
  private int seats = 1;
  private String status = "OPEN";
  private String description = "";

  public String getHostId() {
    return hostId;
  }

  public void setHostId(String value) {
    this.hostId = value;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String value) {
    this.origin = value;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String value) {
    this.destination = value;
  }

  public String getDeparture() {
    return departure;
  }

  public void setDeparture(String value) {
    this.departure = value;
  }

  public int getSeats() {
    return seats;
  }

  public void setSeats(int value) {
    this.seats = value;
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
