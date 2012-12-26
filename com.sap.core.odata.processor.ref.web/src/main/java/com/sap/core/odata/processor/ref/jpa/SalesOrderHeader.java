package com.sap.core.odata.processor.ref.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "T_SALESORDERHEADER")
@NamedQuery(name = "AllSOHeader", query = "select so from SalesOrderHeader so")
public class SalesOrderHeader {

	public SalesOrderHeader() {
		this.creationDate = new Date(System.currentTimeMillis());
		//No arguement constructor
	}
		

	public SalesOrderHeader(int buyerId, String buyerName,
			BuyerAddress buyerAddress, String currencyCode, double netAmount,
			boolean deliveryStatus) {
		
		this();
		this.buyerId = buyerId;
		this.buyerName = buyerName;
		this.buyerAddress = buyerAddress;
		this.currencyCode = currencyCode;
		this.netAmount = netAmount;
		this.deliveryStatus = deliveryStatus;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SO_ID")
	private long soId;

	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column(name = "BUYER_ID")
	private int buyerId;

	@Column(name = "BUYER_NAME")
	private String buyerName;

	@Embedded
	private BuyerAddress buyerAddress;

	@Column(name = "CURRENCY_CODE")
	private String currencyCode;

	@Column(name = "NET_AMOUNT")
	private double netAmount;

	@Column(name = "DELIVERY_STATUS")
	private boolean deliveryStatus;

	@OneToMany(mappedBy = "SalesOrderHeader", cascade = CascadeType.ALL)
	private final List<LineItems> lineItems = new ArrayList<LineItems>();

	public long getSoId() {
		return soId;
	}

	public void setSoId(long soId) {
		this.soId = soId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public BuyerAddress getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(BuyerAddress buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public boolean getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(boolean deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public List<LineItems> getLineItems() {
		return this.lineItems;
	}
}
