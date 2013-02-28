package com.sap.core.odata.processor.ref.jpa;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.*;

@Entity
@Table(name = "T_NOTE")
@IdClass(value = NoteKey.class)
public class Note {
	
	public Note() {
	}	
	
	public Note(String createdBy, Date creationDate, String text) {
		
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
	
	@Column(name = "SO_ID")
	private long soId;
	
	@JoinColumn(name = "SO_ID",referencedColumnName = "SO_ID",insertable = false,updatable = false)
	@ManyToOne
	private SalesOrderHeader salesOrderHeader;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public long getSoId() {
		return soId;
	}

	public void setSoId(long soId) {
		this.soId = soId;
	}
	public SalesOrderHeader getSalesOrderHeader() {
		return this.salesOrderHeader;
	}

	public void setSalesOrderHeader(SalesOrderHeader salesOrderHeader) {
		this.salesOrderHeader = salesOrderHeader;
		//this.salesOrderHeader.getNotes().add(this);
	}
}
