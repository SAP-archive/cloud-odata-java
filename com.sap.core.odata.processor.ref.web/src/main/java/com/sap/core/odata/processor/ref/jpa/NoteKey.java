package com.sap.core.odata.processor.ref.jpa;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NoteKey implements Serializable {

  public NoteKey() {
    //No arg constructor
  }

  public NoteKey(final Calendar creationTime, final Date creationDate, final String createdBy) {
    super();
    this.creationTime = creationTime;
    this.creationDate = creationDate;
    this.createdBy = createdBy;
  }

  private static final long serialVersionUID = 1L;

  private Calendar creationTime;
  private Date creationDate;
  private String createdBy;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((createdBy == null) ? 0 : createdBy.hashCode());
    result = prime * result
        + ((creationDate == null) ? 0 : creationDate.hashCode());
    result = prime * result
        + ((creationTime == null) ? 0 : creationTime.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    NoteKey other = (NoteKey) obj;
    if (createdBy == null) {
      if (other.createdBy != null) {
        return false;
      }
    } else if (!createdBy.equals(other.createdBy)) {
      return false;
    }
    if (creationDate == null) {
      if (other.creationDate != null) {
        return false;
      }
    } else if (!creationDate.equals(other.creationDate)) {
      return false;
    }
    if (creationTime == null) {
      if (other.creationTime != null) {
        return false;
      }
    } else if (!creationTime.equals(other.creationTime)) {
      return false;
    }
    return true;
  }

  public Calendar getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(final Calendar creationTime) {
    this.creationTime = creationTime;
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

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }
}
