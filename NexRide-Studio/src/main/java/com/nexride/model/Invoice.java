package com.nexride.model;

public class Invoice extends BaseEntity {
  private String rentalId = "";
  private String userId = "";
  private String description = "";
  private int amountLkr = 0;
  private String status = "UNPAID";

  public String getRentalId() {
    return rentalId;
  }

  public void setRentalId(String value) {
    this.rentalId = value;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String value) {
    this.userId = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String value) {
    this.description = value;
  }

  public int getAmountLkr() {
    return amountLkr;
  }

  public void setAmountLkr(int value) {
    this.amountLkr = value;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String value) {
    this.status = value;
  }
}
