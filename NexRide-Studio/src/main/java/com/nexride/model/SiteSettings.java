package com.nexride.model;

import java.math.BigDecimal;

public class SiteSettings {
    private BigDecimal lkrPerUsd = BigDecimal.ZERO;
    private String rateSource = "";
    private String rateUpdatedAt = "";

    public BigDecimal getLkrPerUsd() { return lkrPerUsd; }
    public void setLkrPerUsd(BigDecimal value) { lkrPerUsd = value == null ? BigDecimal.ZERO : value; }
    public String getRateSource() { return rateSource; }
    public void setRateSource(String value) { rateSource = value == null ? "" : value; }
    public String getRateUpdatedAt() { return rateUpdatedAt; }
    public void setRateUpdatedAt(String value) { rateUpdatedAt = value == null ? "" : value; }
}
