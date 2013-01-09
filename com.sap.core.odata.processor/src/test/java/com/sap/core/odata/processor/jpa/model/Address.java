package com.sap.core.odata.processor.jpa.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

	public Address() {	
		super();
		
	}

	public Address(short houseNumber, String streetName, String city,
			String country) {
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

	public short getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(short houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
