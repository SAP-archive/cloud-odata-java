package com.sap.core.odata.processor.core.jpa.cud;

public class SalesOrderLineItem {
	
	private int price;

	public SalesOrderLineItem(int price) {
		super();
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
