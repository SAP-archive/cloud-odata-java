package com.sap.core.odata.processor.core.jpa.access.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement;

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

		JPQLContextType contextType = null;
		try {
			if (!uriParserResultView.getStartEntitySet().getName()
					.equals(uriParserResultView.getTargetEntitySet().getName()))
				contextType = JPQLContextType.JOIN;
			else
				contextType = JPQLContextType.SELECT;

		} catch (EdmException e) {
			ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.GENERAL, e);
		}

		// Build JPQL Context
		JPQLContext jpqlContext = JPQLContext.createBuilder(contextType,
				uriParserResultView).build();

		// Build JPQL Statement
		JPQLStatement jpqlStatement = JPQLStatement.createBuilder(jpqlContext)
				.build();
		Query query = null;
		try{
			// Instantiate JPQL
			query = em.createQuery(jpqlStatement.toString());
			if (uriParserResultView.getSkip() != null)
				query.setFirstResult(uriParserResultView.getSkip());
	
			if (uriParserResultView.getTop() != null){
				if(uriParserResultView.getTop() == 0){
					List<T> resultList = new ArrayList<T>();
					return resultList;
				}else{
					query.setMaxResults(uriParserResultView.getTop());
				}
			}
		}catch(IllegalArgumentException e){
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.ERROR_JPQL_QUERY_CREATE, e);
		}
			
		return query.getResultList();

	}

	@Override
	public <T> Object process(GetEntityUriInfo uriParserResultView)
			throws ODataJPAModelException, ODataJPARuntimeException {

		JPQLContextType contextType = null;		
		try {
			if (!uriParserResultView.getStartEntitySet().getName()
					.equals(uriParserResultView.getTargetEntitySet().getName()))
				contextType = JPQLContextType.JOIN_SINGLE;
			else
				contextType = JPQLContextType.SELECT_SINGLE;

		} catch (EdmException e) {
			ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.GENERAL, e);
		}
		
		// Build JPQL Context
		JPQLContext singleSelectContext = JPQLContext.createBuilder(
				contextType, uriParserResultView).build();

		// Build JPQL Statement
		JPQLStatement selectStatement = JPQLStatement.createBuilder(
				singleSelectContext).build();
		Query query = null;
		try{
		// Instantiate JPQL
		query = em.createQuery(selectStatement.toString());
		}catch(IllegalArgumentException e){
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.ERROR_JPQL_QUERY_CREATE, e);
		}
		
		if(query.getResultList().isEmpty())
			return null;
		
		return query.getResultList().get(0);
	}

	
	@Override
	public long process(GetEntitySetCountUriInfo resultsView)
			throws ODataJPAModelException, ODataJPARuntimeException {
		
		JPQLContextType contextType = null;
		try {
			if (!resultsView.getStartEntitySet().getName()
					.equals(resultsView.getTargetEntitySet().getName()))
				contextType = JPQLContextType.JOIN_COUNT;
			else
				contextType = JPQLContextType.SELECT_COUNT;
		} catch (EdmException e) {
			ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.GENERAL, e);
		}
		// Build JPQL Context
		JPQLContext jpqlContext = JPQLContext.createBuilder(contextType,
				resultsView).build();
		// Build JPQL Statement
		JPQLStatement jpqlStatement = JPQLStatement.createBuilder(jpqlContext)
				.build();
		Query query = null;
		try{
			// Instantiate JPQL
			query = em.createQuery(jpqlStatement.toString());
		}catch(IllegalArgumentException e){
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.ERROR_JPQL_QUERY_CREATE, e);
		}
		List<?> resultList = query.getResultList();
		if(resultList != null && resultList.size() == 1)//Expecting exactly one item with count
			return Long.valueOf(resultList.get(0).toString()); 
		else
			return 0;//Invalid value
	}

}
