package com.sap.core.odata.processor.jpa;

import java.util.List;

import javax.persistence.Query;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLStatement;

public class ODataJPAProcessor extends ODataSingleProcessor {

	private ODataJPAContext odataJPAContext;

	public ODataJPAContext getOdataJPAContext() {
		return odataJPAContext;
	}

	public void setOdataJPAContext(ODataJPAContext odataJPAContext) {
		this.odataJPAContext = odataJPAContext;
	}

	@Override
	public ODataResponse readEntitySet(GetEntitySetView uriParserResultView,
			ContentType contentType) throws ODataException {

		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.SELECT, uriParserResultView,odataJPAContext).build();

		// Build JPQL Statement
		JPQLStatement selectStatement = JPQLStatement.createBuilder(
				selectContext).build();

		// Execute JPQL Statement
		Query jpqlQuery = odataJPAContext.getEntityManagerFactory()
				.createEntityManager().createQuery(selectStatement.toString());
		@SuppressWarnings("unchecked")
		List<Object> jpaEntities = jpqlQuery.getResultList();

		// Build OData Response out of a JPA Response
		ODataResponse odataResponse = ODataJPAResponseBuilder.build(
				jpaEntities,uriParserResultView,contentType,odataJPAContext);
		
		return odataResponse;
	}

}
