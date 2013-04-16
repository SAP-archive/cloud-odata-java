/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.ref.model;

/**
 * @author SAP AG
 */
public class Location {
  private String country;
  private City city;

  public Location(final String country, final String postalCode, final String cityName) {
    this.country = country;
    city = new City(postalCode, cityName);
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getCountry() {
    return country;
  }

  public void setCity(final City city) {
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
