package com.sap.core.odata.processor.jpa.access;

import com.sap.core.odata.processor.jpa.ODataJPANameMapper;
import com.sap.core.odata.processor.jpa.access.api.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.access.api.NameMapper;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.jpql.JPQLContextImpl;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;

public class JPAController {
	public static JPAEdmBuilder getJPAEdmBuilder(ODataJPAContext oDataJPAContext) {
		JPAEdmBuilder builder = null;

		builder = new JPAEdmBuilderV2(oDataJPAContext.getPersistenceUnitName(),oDataJPAContext.getEntityManagerFactory());
		return builder;

	}
	
	public static NameMapper getNameMapper( ){
		return new ODataJPANameMapper();
	}
	
	public static JPQLContext getJPQLContext(JPQLContextType contextType){
		JPQLContext jpqlContext = new JPQLContextImpl(contextType);
		return jpqlContext;
	}
}
