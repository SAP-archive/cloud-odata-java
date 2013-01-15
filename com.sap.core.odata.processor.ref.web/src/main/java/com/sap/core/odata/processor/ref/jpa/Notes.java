package com.sap.core.odata.processor.ref.jpa;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "T_NOTES")
@IdClass(value = NotesKey.class)
public class Notes {
	
	public Notes() {
		//No arg constructor
	}	
	
	public Notes(String createdBy, Date creationDate, String text) {
		super();
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.text = text;
	}

	@Id
	private String createdBy;
	
	@Id
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column
	private String text;
	
	@JoinColumn(name = "Sales_Order_Notes_Id", referencedColumnName = "SO_ID", nullable = false, insertable = false, updatable = false)
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

	public SalesOrderHeader getSalesOrderHeader() {
		return salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;
		this.salesOrderHeader.getNotesKey();
	}	
}
