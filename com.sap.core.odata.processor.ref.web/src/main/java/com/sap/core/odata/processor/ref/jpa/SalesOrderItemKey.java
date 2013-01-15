package com.sap.core.odata.processor.ref.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SalesOrderItemKey implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SalesOrderItemKey() {
		//No arguement constructor	
	}
	
	public SalesOrderItemKey(long soId, long liId) {
		super();
		this.soId = soId;
		this.liId = liId;
	}

	@Column(name = "Sales_Order_Id",nullable = false)
	private long soId;
	
	@Column(name = "Sales_Order_Item_Id")
	private long liId;
	
	@Override
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof SalesOrderItemKey))
		{
			SalesOrderItemKey salesOrderItemKey = (SalesOrderItemKey) obj;
			
			if(!(salesOrderItemKey.getSoId()==soId))
			{
				return false;
			}
			
			if(!(salesOrderItemKey.getLiId()==liId))
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
		return (int) (soId + liId);     
	}
	
	public long getSoId() {
		return soId;
	}

	public void setSoId(long soId) {
		this.soId = soId;
	}

	public long getLiId() {
		return liId;
	}

	public void setLiId(long liId) {
		this.liId = liId;
	}
}
