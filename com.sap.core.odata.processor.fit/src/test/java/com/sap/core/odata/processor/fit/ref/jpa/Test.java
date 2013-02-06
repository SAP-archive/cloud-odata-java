package com.sap.core.odata.processor.fit.ref.jpa;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_TEST")
public class Test {
	 public Test()
	 {}
	 
	@EmbeddedId
	private SalesOrderItemKey testKey;
	
	@Column(name = "QUANTITY")
	private int quantity;
	
	@Column(name = "AMOUNT")
	private double amount;
	
	@Column(name = "SALES_ORDER_HEADER_ID")
	private long soId;
	
	@Embedded
	private StorageKey materialDetails;
	
	@JoinColumn(name = "Sales_Order_Header", referencedColumnName = "SO_ID",insertable = false,updatable = false)
	@ManyToOne
	private SalesOrderHeader salesOrderHeader;

	public SalesOrderItemKey getTestKey() {
		return testKey;
	}

	public void setTestKey(SalesOrderItemKey testKey) {
		this.testKey = testKey;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public StorageKey getMaterialDetails() {
		return materialDetails;
	}

	public void setMaterialDetails(StorageKey materialDetails) {
		this.materialDetails = materialDetails;
	}
	public long getSoId() {
		return soId;
	}

	public void setSoId(long soId) {
		this.soId = soId;
	}

	public SalesOrderHeader getSalesOrderHeader() {
		return salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;
	}
	

}
