package com.sap.core.odata.processor.jpa.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SalesOrderItemKey implements Serializable {
		
	private static final long serialVersionUID = 1L;

	public SalesOrderItemKey() {
		//No arguement constructor	
	}
	
	public SalesOrderItemKey(long liId) {
		super();
		this.liId = liId;
	}

	@Column(name = "Sales_Order_Id",nullable = false)
	private long soId;
	
	@Column(name = "Sales_Order_Item_Id")
	private long liId;
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (liId ^ (liId >>> 32));
		result = prime * result + (int) (soId ^ (soId >>> 32));
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
		SalesOrderItemKey other = (SalesOrderItemKey) obj;
		if (liId != other.liId)
			return false;
		if (soId != other.soId)
			return false;
		return true;
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
