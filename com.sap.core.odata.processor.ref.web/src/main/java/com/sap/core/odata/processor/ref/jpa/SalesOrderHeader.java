package com.sap.core.odata.processor.ref.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_SALESORDERHEADER")
@NamedQuery(name = "AllSOHeader", query = "select so from SalesOrderHeader so")
public class SalesOrderHeader {

  public SalesOrderHeader() {
    //No arg constructor
  }

  public SalesOrderHeader(final Date creationDate, final int buyerId, final String buyerName,
      final Address buyerAddress, final String currencyCode, final double netAmount,
      final String deliveryStatus) {
    super();
    this.creationDate = creationDate;
    this.buyerId = buyerId;
    this.buyerName = buyerName;
    this.buyerAddress = buyerAddress;
    this.currencyCode = currencyCode;
    this.deliveryStatus = deliveryStatus;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "SO_ID")
  private long soId;

  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  @Column(name = "BUYER_ID")
  private int buyerId;

  @Column(name = "BUYER_NAME", length = 255)
  private String buyerName;

  @Embedded
  private Address buyerAddress;

  //	@Column(name = "DUMMY_ATTRIBUTE",length = 10)
  //	private String dummyAttributeForExclusion;
  //	
  //	@Embedded
  //	private DummyComplexAttributeForExclusion dummyComplexAttributeForExclusion;

  @Column(name = "CURRENCY_CODE", length = 10)
  private String currencyCode;

  @Column(name = "DELIVERY_STATUS", length = 2)
  private String deliveryStatus;

  @Column(precision = 5)
  private double grossAmount;

  @Column(precision = 8)
  private double netAmount;

  @OneToMany(mappedBy = "salesOrderHeader", cascade = CascadeType.ALL)
  private List<SalesOrderItem> salesOrderItem = new ArrayList<SalesOrderItem>();

  @OneToMany(mappedBy = "salesOrderHeader", cascade = CascadeType.ALL)
  private List<Note> notes = new ArrayList<Note>();

  public long getSoId() {
    return soId;
  }

  public void setSoId(final long soId) {
    this.soId = soId;
  }

  public Date getCreationDate() {
    long dbTime = creationDate.getTime();
    Date originalDate = new Date(dbTime + TimeZone.getDefault().getOffset(dbTime));
    return originalDate;
  }

  public void setCreationDate(final Date creationDate) {
    long originalTime = creationDate.getTime();
    Date newDate = new Date(originalTime - TimeZone.getDefault().getOffset(originalTime));
    this.creationDate = newDate;
  }

  public int getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(final int buyerId) {
    this.buyerId = buyerId;
  }

  public String getBuyerName() {
    return buyerName;
  }

  public void setBuyerName(final String buyerName) {
    this.buyerName = buyerName;
  }

  public Address getBuyerAddress() {
    return buyerAddress;
  }

  public void setBuyerAddress(final Address buyerAddress) {
    this.buyerAddress = buyerAddress;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(final String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getDeliveryStatus() {
    return deliveryStatus;
  }

  public void setDeliveryStatus(final String deliveryStatus) {
    this.deliveryStatus = deliveryStatus;
  }

  public double getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(final double grossAmount) {
    this.grossAmount = grossAmount;
  }

  public double getNetAmount() {
    return netAmount;
  }

  public void setNetAmount(final double netAmount) {
    this.netAmount = netAmount;
  }

  public List<SalesOrderItem> getSalesOrderItem() {
    return salesOrderItem;
  }

  public void setSalesOrderItem(final List<SalesOrderItem> salesOrderItem) {
    this.salesOrderItem = salesOrderItem;
  }

  public List<Note> getNotes() {
    return notes;
  }

  public void setNotes(final List<Note> notes) {
    this.notes = notes;
  }

  //	public String getDummyAttributeForExclusion() {
  //		return dummyAttributeForExclusion;
  //	}
  //
  //	public void setDummyAttributeForExclusion(String dummyAttributeForExclusion) {
  //		this.dummyAttributeForExclusion = dummyAttributeForExclusion;
  //	}
  //
  //	public DummyComplexAttributeForExclusion getDummyComplexAttributeForExclusion() {
  //		return dummyComplexAttributeForExclusion;
  //	}
  //
  //	public void setDummyComplexAttributeForExclusion(
  //			DummyComplexAttributeForExclusion dummyComplexAttributeForExclusion) {
  //		this.dummyComplexAttributeForExclusion = dummyComplexAttributeForExclusion;
  //	}
}
