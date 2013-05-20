package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.*;

@Entity
@Table(name = "T_SALESORDERITEM")
public class SalesOrderItem {

	public SalesOrderItem() {
		// No arg constructor
	}

	public SalesOrderItem(int quantity, double amount, double discount,
			Material material) {
		super();
		this.quantity = quantity;
		this.amount = amount;
		this.discount = discount;
		this.material = material;
	}

	@EmbeddedId
	private SalesOrderItemKey salesOrderItemKey;

	@Column(name = "Material_Id", nullable = false)
	private long matId;

	@Column
	private int quantity;

	@Column
	private double amount;

	@Column
	private double discount;

	@Transient
	private double netAmount;

	@JoinColumn(name = "Material_Id", referencedColumnName = "MATERIAL_ID", insertable = false, updatable = false)
	@ManyToOne
	private Material material;

	@JoinColumn(name = "Sales_Order_Id", referencedColumnName = "SO_ID", insertable = false, updatable = false)
	@ManyToOne
	private SalesOrderHeader salesOrderHeader;

	public SalesOrderItemKey getSalesOrderItemKey() {
		return salesOrderItemKey;
	}

	public void setSalesOrderItemKey(SalesOrderItemKey salesOrderItemKey) {
		this.salesOrderItemKey = salesOrderItemKey;
	}

	public long getMatId() {
		return matId;
	}

	public void setMatId(long matId) {
		this.matId = matId;
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

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public SalesOrderHeader getSalesOrderHeader() {
		return this.salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;
		// this.salesOrderHeader.getSalesOrderItem().add(this);
	}
}
