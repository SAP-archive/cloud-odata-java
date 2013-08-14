package com.sap.core.odata.processor.ref.jpa;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_NOTE")
//@IdClass(value = NoteKey.class)
public class Note {

  public Note() {}

  public Note(final Calendar creationTime, final Date creationDate, final String createdBy,
      final String text) {
    super();
    //		this.creationTime = creationTime;
    //		this.creationDate = creationDate;
    this.createdBy = createdBy;
    this.text = text;
  }

  //	@Id
  //	@Temporal(TemporalType.TIME)
  //	private Calendar creationTime;
  //	
  //	@Id
  //	@Temporal(TemporalType.DATE)
  //	private Date creationDate;
  //	
  @Id
  private String createdBy;

  @Column
  private String text;

  @Column(name = "SO_ID")
  private long soId;

  /*@JoinColumn(name = "SO_ID",referencedColumnName = "SO_ID",insertable = false,updatable = false)*/
  @ManyToOne
  private SalesOrderHeader salesOrderHeader;

  //	public Calendar getCreationTime() {
  //		return creationTime;
  //	}
  //
  //	public void setCreationTime(Calendar creationTime) {
  //		this.creationTime = creationTime;
  //	}

  //	public Date getCreationDate() {
  //		long dbTime = creationDate.getTime();
  //		Date originalDate = new Date(dbTime + TimeZone.getDefault().getOffset(dbTime));
  //		return originalDate;
  //	}
  //
  //	public void setCreationDate(Date creationDate) {
  //		long originalTime = creationDate.getTime();
  //		Date newDate = new Date(originalTime - TimeZone.getDefault().getOffset(originalTime));
  //		this.creationDate = newDate;
  //	}

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public long getSoId() {
    return soId;
  }

  public void setSoId(final long soId) {
    this.soId = soId;
  }

  public SalesOrderHeader getSalesOrderHeader() {
    return salesOrderHeader;
  }

  public void setSalesOrderHeader(final SalesOrderHeader salesOrderHeader) {
    this.salesOrderHeader = salesOrderHeader;
    //this.salesOrderHeader.getNotes().add(this);
  }
}
