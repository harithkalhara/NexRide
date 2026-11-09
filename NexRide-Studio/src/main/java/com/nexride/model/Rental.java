package com.nexride.model;

public class Rental extends BaseEntity {
  private String userId = "";
  private String vehicleId = "";
  private String startDate = "";
  private String endDate = "";
  private int total = 0;
  private String status = "PENDING";
  private String note = "";

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

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String value) {
    this.startDate = value;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String value) {
    this.endDate = value;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int value) {
    this.total = value;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String value) {
    this.status = value;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String value) {
    this.note = value;
  }
}
