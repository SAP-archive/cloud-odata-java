package com.sap.core.odata.processor.ref;

import com.sap.core.odata.processor.jpa.ODataJPAServiceFactory;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

public class JPAReferenceServiceFactory extends ODataJPAServiceFactory{
	
	private static final String PUNIT_NAME = "salesorderprocessing";
	
	@Override
	public ODataJPAContext initializeJPAContext() {
		ODataJPAContext oDataJPAContext= this.getODataJPAContext();
		
		oDataJPAContext.setPersistenceUnitName(PUNIT_NAME);

		return oDataJPAContext;
	}

}
