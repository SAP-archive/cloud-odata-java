package com.sap.core.odata.processor.jpa;

import java.util.List;

import javax.persistence.Query;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.processor.jpa.access.JPAController;
import com.sap.core.odata.processor.jpa.access.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.access.api.JPQLSelectContext;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatement;
import com.sap.core.odata.processor.jpa.access.api.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

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
    
	    //Get JPQLContext
		JPQLSelectContext selectContext = (JPQLSelectContext) JPAController.getJPQLContext(JPQLContextType.SELECT);
		selectContext.setJPAEntityName(uriParserResultView.getStartEntitySet().getEntityType().getName());
		
		//Build JPQL Statement
		JPQLStatementBuilder statementBuilder = JPQLStatement.setJPQLContext(selectContext);
		JPQLStatement selectStatement = statementBuilder.build();
		
		Query jpqlQuery = odataJPAContext.getEntityManagerFactory().createEntityManager().createQuery(selectStatement.toString());
		List<Object> jpaEntities = jpqlQuery.getResultList();
		
		ODataResponseBuilder odataResponseBuilder = ODataResponse.status(HttpStatusCodes.OK);
	    
		return odataResponseBuilder.build();
	  }


}
