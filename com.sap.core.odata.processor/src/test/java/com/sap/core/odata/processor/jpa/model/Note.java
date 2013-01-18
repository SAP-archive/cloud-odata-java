package com.sap.core.odata.processor.jpa.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "T_NOTE")
@IdClass(value = NoteKey.class)
public class Note {
	
	public Note() {
		//No arg constructor
	}	
	
	public Note(String createdBy, Date creationDate, String text, long soId) {
		super();
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.text = text;
		this.soId = soId;
	}

	@Id
	private String createdBy;
	
	@Id
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column
	private String text;
	
	@Column(name = "Sales_Order_Note_Id")
	private long soId;
	
	@JoinColumn(name = "Sales_Order_Note_Id", referencedColumnName = "SO_ID", nullable = false, insertable = false, updatable = false)
	@OneToOne
	private SalesOrderHeader salesOrderHeader;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getSoId() {
		return soId;
	}

	public void setSalesOrderId(long soId) {
		this.soId = soId;
	}
	public SalesOrderHeader getSalesOrderHeader() {
		return salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;		
	}	
}
