package com.sap.core.odata.processor.jpa.access;

import com.sap.core.odata.processor.jpa.ODataJPANameMapper;
import com.sap.core.odata.processor.jpa.access.api.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.access.api.NameMapper;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

public class JPAController {
	public static JPAEdmBuilder getJPAEdmBuilder(ODataJPAContext oDataJPAContext) {
		JPAEdmBuilder builder = null;

		builder = new JPAEdmBuilderV2(oDataJPAContext.getPersistenceUnitName(),oDataJPAContext.getEntityManagerFactory());
		return builder;

	}
	
	public static NameMapper getNameMapper( ){
		return new ODataJPANameMapper();
	}

}
