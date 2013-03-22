package com.sap.core.odata.processor.core.jpa.model;

import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;

public class JPAEdmMappingImpl extends Mapping implements JPAEdmMapping {

	private String columnName = null;
	private Class<?> type = null;

	@Override
	public void setJPAColumnName(String name) {
		this.columnName = name;

	}

	@Override
	public String getJPAColumnName() {
		return this.columnName;
	}

	@Override
	public void setJPAType(Class<?> type) {
		this.type = type;

	}

	@Override
	public Class<?> getJPAType() {
		return type;
	}

}
