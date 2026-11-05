package com.nexride.model;

public class Motorcycle extends Vehicle {
  private int engineCc = 125;

  public Motorcycle() {}

  @Override
  public String getKind() {
    return "MOTORCYCLE";
  }

  public int getEngineCc() {
    return engineCc;
  }

  public void setEngineCc(int value) {
    this.engineCc = value;
  }
}
