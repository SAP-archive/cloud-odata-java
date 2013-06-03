package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DummyComplexAttributeForExclusion {

  public DummyComplexAttributeForExclusion() {
    super();

  }

  public DummyComplexAttributeForExclusion(final short houseNumber, final String streetName, final String city,
      final String country) {
    this();
    this.houseNumber = houseNumber;
    this.streetName = streetName;
    this.city = city;
    this.country = country;
  }

  @Column(name = "HOUSE_NUMBER")
  private short houseNumber;

  @Column(name = "STREET_NAME")
  private String streetName;

  @Column(name = "CITY")
  private String city;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "DUMMY_ATTRIBUTE", length = 10)
  private String dummyAttributeForExclusion;

  public short getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(final short houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(final String streetName) {
    this.streetName = streetName;
  }

  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getDummyAttributeForExclusion() {
    return dummyAttributeForExclusion;
  }

  public void setDummyAttributeForExclusion(final String dummyAttributeForExclusion) {
    this.dummyAttributeForExclusion = dummyAttributeForExclusion;
  }
}
