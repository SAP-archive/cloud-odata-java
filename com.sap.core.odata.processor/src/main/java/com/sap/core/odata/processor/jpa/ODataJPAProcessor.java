package com.sap.core.odata.processor.jpa;

import java.util.List;

import javax.persistence.Query;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLStatement;

public class ODataJPAProcessor extends ODataSingleProcessor {
	
	private ODataContext odataContext = this.getContext();
	private ODataJPAContext odataJPAContext;
	
	
	public ODataJPAContext getOdataJPAContext() {
		return odataJPAContext;
	}
	public void setOdataJPAContext(ODataJPAContext odataJPAContext) {
		this.odataJPAContext = odataJPAContext;
	}
	
	@Override
	  public ODataResponse readEntitySet(GetEntitySetView uriParserResultView, ContentType contentType) throws ODataException {
    
	    //Build JPQLContext
		JPQLContext selectContext = JPQLContext.setJPQLContextType(JPQLContextType.SELECT).build( );
		//selectContext.setJPAEntityName(uriParserResultView.getStartEntitySet().getEntityType().getName());
		
		//Build JPQL Statement
		JPQLStatement selectStatement = JPQLStatement.setJPQLContext(selectContext).build();
		
		//Execute JPQL Statement
		Query jpqlQuery = odataJPAContext.getEntityManagerFactory().createEntityManager().createQuery(selectStatement.toString());
		List<Object> jpaEntities = jpqlQuery.getResultList();
		
		//Build OData Response
		//ODataResponse odataResponse = ODataJPAResponseBuilder.build(jpaEntities,uriParserResultView);
		return null;
	  }


}
