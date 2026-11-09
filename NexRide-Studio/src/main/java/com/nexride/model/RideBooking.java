package com.nexride.model;

public class RideBooking extends BaseEntity {
  private String userId = "";
  private String rideId = "";
  private String status = "PENDING";

  public String getUserId() {
    return userId;
  }

  public void setUserId(String value) {
    this.userId = value;
  }

  public String getRideId() {
    return rideId;
  }

  public void setRideId(String value) {
    this.rideId = value;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String value) {
    this.status = value;
  }
}
