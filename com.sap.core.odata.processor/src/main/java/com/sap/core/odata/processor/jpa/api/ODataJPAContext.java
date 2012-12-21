package com.sap.core.odata.processor.jpa.api;

import javax.persistence.EntityManagerFactory;

import com.sap.core.odata.processor.jpa.access.api.NameMapper;

public interface ODataJPAContext{
	
	
	public String getPersistenceUnitName();
	public void setPersistenceUnitName(String pUnitName);
	
	public NameMapper getNameMapper( );
	public void setNameMapper(NameMapper nameMapper);
	
	public EntityManagerFactory getEntityManagerFactory( );
	public void setEntityManagerFactory(EntityManagerFactory emf);
	
}
