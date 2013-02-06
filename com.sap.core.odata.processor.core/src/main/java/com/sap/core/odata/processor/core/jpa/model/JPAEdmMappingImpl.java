package com.sap.core.odata.processor.core.jpa.model;

import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.api.model.JPAEdmMapping;

public class JPAEdmMappingImpl extends Mapping implements JPAEdmMapping{
	
	private String columnName = null;
	@Override
	public void setJPAColumnName(String name) {
		this.columnName = name;
		
	}

	@Override
	public String getJPAColumnName() {
		return this.columnName;
	}
	
}
