package com.sap.core.odata.ref.model;

public class Location {
  private String country;
  private City city;

  public Location() {
    this(null, null, null);
  }

  public Location(String country, String postalCode, String cityName) {
    this.country = country;
    this.city = new City(postalCode, cityName);
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountry() {
    return country;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public City getCity() {
    return city;
  }

  @Override
  public String toString() {
    return String.format("%s, %s", country, city.toString());
  }

}
