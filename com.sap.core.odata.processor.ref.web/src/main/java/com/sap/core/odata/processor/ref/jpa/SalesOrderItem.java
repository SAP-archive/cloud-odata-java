package com.sap.core.odata.processor.ref.jpa;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "T_SALESORDERITEM")
public class SalesOrderItem {

  public SalesOrderItem() {
    // No arg constructor
  }

  public SalesOrderItem(final int quantity, final double amount, final double discount,
      final Material material) {
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

  public void setSalesOrderItemKey(final SalesOrderItemKey salesOrderItemKey) {
    this.salesOrderItemKey = salesOrderItemKey;
  }

  public long getMatId() {
    return matId;
  }

  public void setMatId(final long matId) {
    this.matId = matId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(final int quantity) {
    this.quantity = quantity;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(final double amount) {
    this.amount = amount;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(final double discount) {
    this.discount = discount;
  }

  public double getNetAmount() {
    return netAmount;
  }

  public void setNetAmount(final double netAmount) {
    this.netAmount = netAmount;
  }

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(final Material material) {
    this.material = material;
  }

  public SalesOrderHeader getSalesOrderHeader() {
    return salesOrderHeader;
  }

  public void setSalesOrderHeader(final SalesOrderHeader salesOrderHeader) {
    this.salesOrderHeader = salesOrderHeader;
    // this.salesOrderHeader.getSalesOrderItem().add(this);
  }
}
