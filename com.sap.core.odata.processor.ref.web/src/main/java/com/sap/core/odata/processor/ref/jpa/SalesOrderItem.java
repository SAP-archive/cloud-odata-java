package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.*;

@Entity
@Table(name = "T_SALESORDERITEM")
public class SalesOrderItem {

	public SalesOrderItem() {
		super();
		//No arg const.
	}

	
	public SalesOrderItem(String productName, int productId, double price) {
		super();
		this.productName = productName;
		this.productId = productId;
		this.price = price;
	}

	@EmbeddedId
	private SalesOrderItemKey salesOrderItemKey;

	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Column(name = "PRODUCT_ID")
	private int productId;
	
	@Column(name = "PRICE")
	private double price;
		
	@JoinColumn(name = "Sales_Order_Id", referencedColumnName = "SO_ID", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private SalesOrderHeader salesOrderHeader;

	public SalesOrderItemKey getSalesOrderItemKey() {
		return salesOrderItemKey;
	}

	public void setSalesOrderItemKey(SalesOrderItemKey salesOrderItemKey) {
		this.salesOrderItemKey = salesOrderItemKey;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public SalesOrderHeader getSalesOrderHeader() {
		return this.salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;
		this.salesOrderHeader.getSalesOrderItem().add(this);
	}

}
