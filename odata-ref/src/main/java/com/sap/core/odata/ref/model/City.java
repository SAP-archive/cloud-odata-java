/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.ref.model;

/**
 * @author SAP AG
 */
public class City {

  private String postalCode;
  private String cityName;

  public City(final String postalCode, final String name) {
    this.postalCode = postalCode;
    cityName = name;
  }

  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setCityName(final String cityName) {
    this.cityName = cityName;
  }

  public String getCityName() {
    return cityName;
  }

  @Override
  public String toString() {
    return String.format("%s, %s", cityName, postalCode);
  }

}
