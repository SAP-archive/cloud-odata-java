package com.sap.core.odata.processor.jpa.api;

import javax.persistence.EntityManagerFactory;
import com.sap.core.odata.api.processor.ODataContext;

public interface ODataJPAContext{
	
	
	public String getPersistenceUnitName();
	public void setPersistenceUnitName(String pUnitName);
	

	public EntityManagerFactory getEntityManagerFactory( );
	public void setEntityManagerFactory(EntityManagerFactory emf);
	public void setODataContext(ODataContext ctx);
	public ODataContext getODataContext();
	
}
