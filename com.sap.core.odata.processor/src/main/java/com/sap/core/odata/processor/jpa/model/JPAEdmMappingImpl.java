package com.sap.core.odata.processor.jpa.model;

import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmMapping;

public class JPAEdmMappingImpl extends Mapping implements JPAEdmMapping{
	
	private String columnName = null;
	@Override
	public void setColumnName(String name) {
		this.columnName = name;
		
	}

	@Override
	public String getColumnName() {
		return this.columnName;
	}
	
}
