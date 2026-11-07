package com.nexride.Model;

public class SiteSettings {

    // Instance Variables
    private String logoUrl = "/assets/nexride-mark.svg";
    private String tagline = "Your island. Your pace.";
    private String contactEmail = "";
    private String contactPhone = "";
    private String address = "";
    private String hours = "";
    private String instagramUrl = "";
    private String facebookUrl = "";
    private java.math.BigDecimal lkrPerUsd = new java.math.BigDecimal("330.8016");
    private String rateUpdatedAt = "2026-10-14T00:02:32Z";
    private String rateSource = "ExchangeRate-API";

    // Getters and Setters
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String value) {
        this.logoUrl = value;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String value) {
        this.tagline = value;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String value) {
        this.contactEmail = value;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String value) {
        this.contactPhone = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String value) {
        this.hours = value;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String value) {
        this.instagramUrl = value;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String value) {
        this.facebookUrl = value;
    }

    public java.math.BigDecimal getLkrPerUsd() {
        return lkrPerUsd;
    }

    public void setLkrPerUsd(java.math.BigDecimal value) {
        this.lkrPerUsd = value;
    }

    public String getRateUpdatedAt() {
        return rateUpdatedAt;
    }

    public void setRateUpdatedAt(String value) {
        this.rateUpdatedAt = value;
    }

    public String getRateSource() {
        return rateSource;
    }

    public void setRateSource(String value) {
        this.rateSource = value;
    }
}
