package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.Embeddable;

@Embeddable
public class TelephoneNumber {
	
	//@Column(name = "PhoneNumber")
	private int phoneNumber;
	
	public TelephoneNumber()
	{
		super();
	}

	public TelephoneNumber(int phoneNumber) {
		super();
		this.phoneNumber = phoneNumber;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
