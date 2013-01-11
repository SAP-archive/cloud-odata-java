package com.sap.core.odata.processor.jpa.access;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.access.api.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLStatement;

public class JPAProcessorImpl implements JPAProcessor {
	
	ODataJPAContext oDataJPAContext;
	EntityManager em;
	
	public JPAProcessorImpl(ODataJPAContext oDataJPAContext) {
		this.oDataJPAContext = oDataJPAContext;
		em = oDataJPAContext.getEntityManagerFactory().createEntityManager();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> process(GetEntitySetUriInfo uriParserResultView) throws ODataJPAModelException, ODataJPARuntimeException {
		
		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.SELECT, uriParserResultView)
				.build();

		// Build JPQL Statement
		JPQLStatement selectStatement = JPQLStatement.createBuilder(
				selectContext).build();
		
		//Instantiate JPQL
		Query query = em.createQuery(selectStatement.toString());
		
		return query.getResultList();

		
	}

	@Override
	public Object process(GetEntityUriInfo uriParserResultView) {
		// TODO Auto-generated method stub
		return null;
	}

}
