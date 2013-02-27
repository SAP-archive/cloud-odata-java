package com.sap.core.odata.processor.ref;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.ODataJPAServiceFactory;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class JPAReferenceServiceFactory extends ODataJPAServiceFactory{
	
	private static final String PUNIT_NAME = "salesorderprocessing";
		
	@Override
	public ODataJPAContext initializeJPAContext() throws ODataJPARuntimeException  {
		ODataJPAContext oDataJPAContext= this.getODataJPAContext();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNIT_NAME);
		
		oDataJPAContext.setEntityManagerFactory(emf);
		oDataJPAContext.setPersistenceUnitName(PUNIT_NAME); 
		oDataJPAContext.setJPAEdmNameMappingModel("SalesOrderProcessingMappingModel.xml");
		return oDataJPAContext;
	}


}
