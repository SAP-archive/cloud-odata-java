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
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof NotesKey))
		{
			NotesKey notesKey = (NotesKey)obj;
			
			if(!(notesKey.getCreatedBy()==createdBy))
			{
				return false;
			}
			
			if(!(notesKey.getCreationDate()==creationDate))
			{
				return false;             
			}
			
			return true;
		}
		
		return false;
	}	
	
	@Override
	public int hashCode() 
	{
		return ((Integer.parseInt(createdBy)) + (int)(creationDate.getTime()));     
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

