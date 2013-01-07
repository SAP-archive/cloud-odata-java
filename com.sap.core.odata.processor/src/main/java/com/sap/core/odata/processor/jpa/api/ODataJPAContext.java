package com.sap.core.odata.processor.jpa.api;

import javax.persistence.EntityManagerFactory;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;

public interface ODataJPAContext{
	
	
	public String getPersistenceUnitName();
	public void setPersistenceUnitName(String pUnitName);
	
	public void setODataProcessor(ODataProcessor processor);
	public ODataProcessor getODataProcessor();
	
	public void setEdmProvider(EdmProvider edmProvider);
	public EdmProvider getEdmProvider();

	public EntityManagerFactory getEntityManagerFactory( );
	public void setEntityManagerFactory(EntityManagerFactory emf);
	public void setODataContext(ODataContext ctx);
	public ODataContext getODataContext();
	
}
