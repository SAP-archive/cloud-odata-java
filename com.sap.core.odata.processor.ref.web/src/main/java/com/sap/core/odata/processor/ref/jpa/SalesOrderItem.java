package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.*;

@Entity
@Table(name = "T_LINEITEMS")
public class SalesOrderItem {

	public SalesOrderItem() {
		super();
		//No arg const.
	}

	public SalesOrderItem(String productName) {
		this();
		this.productName = productName;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LINE_ITEM_ID")
	private long liId;

	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Column(name = "PRODUCT_ID")
	private int productId;
	
	@Column(name = "PRODUCT_PRICE")
	private double price;
	
	@JoinColumn(name = "SO_LINE_ITEM_ID", referencedColumnName = "SO_ID")
	@ManyToOne
	private SalesOrderHeader salesOrderHeader;

	public long getLiId() {
		return liId;
	}

	public void setLiId(long liId) {
		this.liId = liId;
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
	
	public double getProductPrice() {
		return price;
	}

	public void setProductPrice(double price) {
		this.price = price;
	}
	
	public SalesOrderHeader getSalesOrderHeader() {
		return this.salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;
		this.salesOrderHeader.getLineItems().add(this);
	}

}
