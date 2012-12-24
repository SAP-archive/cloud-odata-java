package com.sap.core.odata.processor.ref.jpa;

import java.util.Calendar;

import javax.persistence.*;

@Entity
@Table(name = "T_SALESORDERHEADER")
@NamedQuery(name = "AllSOHeader", query = "select so from SalesOrderHeader so")
public class SalesOrderHeader {

	@Id
	private long id;
	@Column(name = "NAME")
	  private String name;
	@Column(name = "DESCRIPTION")
	  private String description;
	@Column(name = "PRICE")
		private int price;
	@Temporal(TemporalType.TIMESTAMP)
		private Calendar deliveryDate;

	public Calendar getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Calendar deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}