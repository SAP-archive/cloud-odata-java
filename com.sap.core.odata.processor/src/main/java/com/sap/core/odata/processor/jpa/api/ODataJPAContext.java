package com.sap.core.odata.processor.jpa.api;

import com.sap.core.odata.processor.jpa.access.api.NameMapper;

public interface ODataJPAContext{
	
	
	public String getPersistenceUnitName();
	public void setPersistenceUnitName(String pUnitName);
	
	public NameMapper getNameMapper( );
	public void setNameMapper(NameMapper nameMapper);
	
}
