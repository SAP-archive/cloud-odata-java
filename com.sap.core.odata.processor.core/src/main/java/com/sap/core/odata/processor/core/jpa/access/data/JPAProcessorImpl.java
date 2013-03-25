package com.sap.core.odata.processor.core.jpa.access.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement;
import com.sap.core.odata.processor.core.jpa.cud.JPACreateContext;


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
		return readEntity(uriParserResultView);
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

	@Override
	public <T> Object process(PostUriInfo createView, InputStream content, String requestedContentType)
			throws ODataJPAModelException, ODataJPARuntimeException {
		JPACreateContext jpaCreateContext = new JPACreateContext(em.getEntityManagerFactory().getMetamodel());		
		Object createObject = jpaCreateContext.build(createView, content, requestedContentType);
		try{
		em.getTransaction().begin();
		em.persist(createObject);
		em.flush();
		em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.ERROR_JPQL_CREATE_CREATE, e);
		}		
//		Object createdObject = readEntity(createView);
		if(em.contains(createObject)){
			return createObject;
		}		
		return null;
	}

//	@Override
//	public boolean process(DeleteUriInfo deleteView)
//			throws ODataJPAModelException, ODataJPARuntimeException {
//		// TODO Auto-generated method stub
//		return false;
//	}
	
	/**
     * This is a common method to be used by read and delete process.
     * 
      * @param uriParserResultView
     * @return
     * @throws ODataJPAModelException
     * @throws ODataJPARuntimeException
     */
     private Object readEntity(Object uriParserResultView)
                   throws ODataJPAModelException, ODataJPARuntimeException {
            JPQLContextType contextType = null;      
            Object selectedObject = null;
            
            if(uriParserResultView instanceof DeleteUriInfo || uriParserResultView instanceof GetEntityUriInfo || uriParserResultView instanceof PostUriInfo){
                   try {
                         if(uriParserResultView instanceof DeleteUriInfo){
                                uriParserResultView = ((DeleteUriInfo)uriParserResultView);
                                       if (!((DeleteUriInfo) uriParserResultView).getStartEntitySet().getName()
                                                     .equals(((DeleteUriInfo) uriParserResultView).getTargetEntitySet().getName()))
                                              contextType = JPQLContextType.JOIN_SINGLE;
                                       else
                                              contextType = JPQLContextType.SELECT_SINGLE;
                         }else if(uriParserResultView instanceof PostUriInfo){
                             uriParserResultView = ((PostUriInfo)uriParserResultView);
                             if (!((PostUriInfo) uriParserResultView).getStartEntitySet().getName()
                                           .equals(((PostUriInfo) uriParserResultView).getTargetEntitySet().getName()))
                                    contextType = JPQLContextType.JOIN_SINGLE;
                             else
                                    contextType = JPQLContextType.SELECT_SINGLE;
                         }else{
                                uriParserResultView = ((GetEntityUriInfo)uriParserResultView);
                                if (!((GetEntityUriInfo) uriParserResultView).getStartEntitySet().getName()
                                              .equals(((GetEntityUriInfo) uriParserResultView).getTargetEntitySet().getName()))
                                       contextType = JPQLContextType.JOIN_SINGLE;
                                else
                                       contextType = JPQLContextType.SELECT_SINGLE;
                         }
                   } catch (EdmException e) {
                         ODataJPARuntimeException.throwException(
                                       ODataJPARuntimeException.GENERAL, e);
                   }
                   
                   // Build JPQL Context
                   JPQLContext selectJPQLContext = JPQLContext.createBuilder(
                                contextType, uriParserResultView).build();
                   
                   // Build JPQL Statement
                   JPQLStatement selectJPQLStatement = JPQLStatement.createBuilder(
                                selectJPQLContext).build();
                   Query query = null;
                   try{
                         // Instantiate JPQL
                         query = em.createQuery(selectJPQLStatement.toString());
                   }catch(IllegalArgumentException e){
                         throw ODataJPARuntimeException.throwException(
                                       ODataJPARuntimeException.ERROR_JPQL_QUERY_CREATE, e);
                   }
                   
                   if(!query.getResultList().isEmpty()){
                         selectedObject = query.getResultList().get(0);
                   }
                   
            }
            return selectedObject;
     }


}
