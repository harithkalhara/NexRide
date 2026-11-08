package com.nexride.model;

public class CheckoutAttempt extends BaseEntity {
  private String invoiceId = "",
      userId = "",
      currency = "LKR",
      amount = "",
      rate = "",
      status = "PENDING",
      providerReference = "";
  private int lkrAmount;

  public int getLkrAmount() {
    return lkrAmount;
  }

  public void setLkrAmount(int v) {
    lkrAmount = v;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public void setInvoiceId(String v) {
    invoiceId = v;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String v) {
    userId = v;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String v) {
    currency = v;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String v) {
    amount = v;
  }

  public String getRate() {
    return rate;
  }

  public void setRate(String v) {
    rate = v;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String v) {
    status = v;
  }

  public String getProviderReference() {
    return providerReference;
  }

  public void setProviderReference(String v) {
    providerReference = v;
  }
}
