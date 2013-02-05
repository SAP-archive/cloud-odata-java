package com.sap.core.odata.processor.ref.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StorageKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public StorageKey() {
		//No arg Constructor
	}
	
	@Column(name = "MATERIAL_ID",nullable = false)
	private long materialId;
	
	@Column(name = "STORE_NAME",nullable = false)
	private String storeName;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (materialId ^ (materialId >>> 32));
		result = prime * result
				+ ((storeName == null) ? 0 : storeName.hashCode());
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
		StorageKey other = (StorageKey) obj;
		if (materialId != other.materialId)
			return false;
		if (storeName == null) {
			if (other.storeName != null)
				return false;
		} else if (!storeName.equals(other.storeName))
			return false;
		return true;
	}

	public long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(long materialId) {
		this.materialId = materialId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
}
