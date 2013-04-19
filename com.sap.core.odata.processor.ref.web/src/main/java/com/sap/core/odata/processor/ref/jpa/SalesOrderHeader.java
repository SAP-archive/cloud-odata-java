package com.sap.core.odata.processor.ref.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.*;

@Entity
@Table(name = "T_SALESORDERHEADER")
@NamedQuery(name = "AllSOHeader", query = "select so from SalesOrderHeader so")
public class SalesOrderHeader {

	public SalesOrderHeader() {
		//No arg constructor
	}		

	public SalesOrderHeader(Date creationDate,int buyerId, String buyerName,
			Address buyerAddress, String currencyCode, double netAmount,
			String deliveryStatus,Calendar calendar) {
		super();
		this.creationDate = creationDate;
		this.buyerId = buyerId;
		this.buyerName = buyerName;
		this.buyerAddress = buyerAddress;
		this.currencyCode = currencyCode;
		this.deliveryStatus = deliveryStatus;
		this.creationCalendar = calendar;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SO_ID")
	private long soId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar creationCalendar;

	@Column(name = "BUYER_ID")
	private int buyerId;

	@Column(name = "BUYER_NAME",length = 255)
	private String buyerName;

	@Embedded
	private Address buyerAddress;

	@Column(name = "CURRENCY_CODE",length = 10)
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

	public void setSoId(long soId) {
		this.soId = soId;
	}

	public Date getCreationDate() {
		long dbTime = creationDate.getTime();
		Date originalDate = new Date(dbTime + TimeZone.getDefault().getOffset(dbTime));
		return originalDate;
	}

	public void setCreationDate(Date creationDate) {
		long originalTime = creationDate.getTime();
		Date newDate = new Date(originalTime - TimeZone.getDefault().getOffset(originalTime));
		this.creationDate = newDate;
	}

	public int getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Address getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(Address buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	
	public double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public List<SalesOrderItem> getSalesOrderItem() {
		return salesOrderItem;
	}

	public void setSalesOrderItem(List<SalesOrderItem> salesOrderItem) {
		this.salesOrderItem = salesOrderItem;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Calendar getCreationCalendar() {
		return creationCalendar;
	}

	public void setCreationCalendar(Calendar creationCalendar) {
		this.creationCalendar = creationCalendar;
	}		
}
