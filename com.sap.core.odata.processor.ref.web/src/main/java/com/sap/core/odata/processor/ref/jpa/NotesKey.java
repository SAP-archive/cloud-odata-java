package com.sap.core.odata.processor.ref.jpa;

import java.io.Serializable;
import java.util.Date;

public class NotesKey implements Serializable {
	
	
	/**
	 * 
	 */
	public NotesKey() {
		//No arg constructor
	}
	
	public NotesKey(String createdBy, Date creationDate) {
		super();
		this.createdBy = createdBy;
		this.creationDate = creationDate;
	}

	private static final long serialVersionUID = 1L;

	private String createdBy;
	private Date creationDate;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotesKey other = (NotesKey) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		return true;
	}
	
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
}

