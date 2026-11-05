package com.nexride.model;

public class Bicycle extends Vehicle {
  private int gearCount = 7;
  private String frameSize = "M";

  public Bicycle() {}

  @Override
  public String getKind() {
    return "BICYCLE";
  }

  public int getGearCount() {
    return gearCount;
  }

  public void setGearCount(int value) {
    this.gearCount = value;
  }

  public String getFrameSize() {
    return frameSize;
  }

  public void setFrameSize(String value) {
    this.frameSize = value;
  }
}
