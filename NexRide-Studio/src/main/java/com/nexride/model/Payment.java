package com.nexride.model;

public class Payment extends BaseEntity {
  private String invoiceId = "";
  private String userId = "";
  private int amountLkr = 0;
  private String chargedAmount = "";
  private String currency = "LKR";
  private String method = "";
  private String reference = "";
  private String gatewayStatus = "SETTLED";

  public String getInvoiceId() {
    return invoiceId;
  }

  public void setInvoiceId(String value) {
    this.invoiceId = value;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String value) {
    this.userId = value;
  }

  public int getAmountLkr() {
    return amountLkr;
  }

  public void setAmountLkr(int value) {
    this.amountLkr = value;
  }

  public String getChargedAmount() {
    return chargedAmount;
  }

  public void setChargedAmount(String value) {
    this.chargedAmount = value;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String value) {
    this.currency = value;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String value) {
    this.method = value;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String value) {
    this.reference = value;
  }

  public String getGatewayStatus() {
    return gatewayStatus;
  }

  public void setGatewayStatus(String value) {
    this.gatewayStatus = value;
  }
}
