package com.sap.core.odata.processor.api.jpa.model;

/**
 * This class provides view to the implementation class which sets and gets EDM Mapping.
 * @author AG
 *
 */
public interface JPAEdmMapping {
	public void setJPAColumnName(String name);
	public String getJPAColumnName( );
}
