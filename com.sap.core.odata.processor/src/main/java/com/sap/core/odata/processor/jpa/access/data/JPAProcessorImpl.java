package com.sap.core.odata.processor.jpa.access.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPAProcessorImpl implements JPAProcessor {

	ODataJPAContext oDataJPAContext;
	EntityManager em;

	public JPAProcessorImpl(ODataJPAContext oDataJPAContext) {
		this.oDataJPAContext = oDataJPAContext;
		em = oDataJPAContext.getEntityManagerFactory().createEntityManager();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> process(GetEntitySetUriInfo uriParserResultView)
			throws ODataJPAModelException, ODataJPARuntimeException {

		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.SELECT, uriParserResultView).build();

		// Build JPQL Statement
		JPQLStatement selectStatement = JPQLStatement.createBuilder(
				selectContext).build();

		// Instantiate JPQL
		Query query = em.createQuery(selectStatement.toString());
		if (uriParserResultView.getSkip() != null)
			query.setFirstResult(uriParserResultView.getSkip());

		if (uriParserResultView.getTop() != null)
			query.setMaxResults(uriParserResultView.getTop());

		return query.getResultList();

	}

	@Override
	public Object process(GetEntityUriInfo uriParserResultView)
			throws ODataJPAModelException, ODataJPARuntimeException {
		// Build JPQL Context
		JPQLContext singleSelectContext = JPQLContext.createBuilder(
				JPQLContextType.SELECT_SINGLE, uriParserResultView).build();

		// Build JPQL Statement
		JPQLStatement selectStatement = JPQLStatement.createBuilder(
				singleSelectContext).build();

		// Instantiate JPQL
		Query query = em.createQuery(selectStatement.toString());

		return query.getResultList().get(0);
	}

}
