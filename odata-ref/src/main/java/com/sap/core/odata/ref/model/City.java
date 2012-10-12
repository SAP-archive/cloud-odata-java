package com.sap.core.odata.ref.model;

public class City {

  private String postalCode;
  private String cityName;

  public City() {
    this(null, null);
  }

  public City(String postalCode, String name) {
    this.postalCode = postalCode;
    this.cityName = name;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getCityName() {
    return cityName;
  }

  @Override
  public String toString() {
    return String.format("%s, %s ", cityName, postalCode);
  }

}
