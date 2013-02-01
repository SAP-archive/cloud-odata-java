package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.ODataJPAContextImpl;
import com.sap.core.odata.processor.jpa.api.factory.JPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAAccessFactory;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.factory.ODataJPAFactoryImpl;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContext.JPQLSelectContextBuilder;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleContext.JPQLSelectSingleContextBuilder;

public class JPQLBuilderFactoryTest {
	
	@Test
	public void testGetStatementBuilderFactoryforSelect() throws ODataException{
		
		GetEntitySetUriInfo getEntitySetView = getUriInfo();
		
		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.SELECT, getEntitySetView).build();
		JPQLStatementBuilder statementBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getStatementBuilder(selectContext);
		
		assertTrue(statementBuilder instanceof JPQLSelectStatementBuilder);
				
	}
	
	@Test
	public void testGetStatementBuilderFactoryforSelectSingle() throws ODataException{
		
		GetEntityUriInfo getEntityView = getEntityUriInfo();
		
		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.SELECT_SINGLE, getEntityView).build();
		JPQLStatementBuilder statementBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getStatementBuilder(selectContext);
		
		assertTrue(statementBuilder instanceof JPQLSelectSingleStatementBuilder);
				
	}
	
	@Test
	public void testGetStatementBuilderFactoryforJoinSelect() throws ODataException{
		
		GetEntitySetUriInfo getEntitySetView = getUriInfo();
		
		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.JOIN, getEntitySetView).build();
		JPQLStatementBuilder statementBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getStatementBuilder(selectContext);
		
		assertTrue(statementBuilder instanceof JPQLJoinStatementBuilder);
				
	}
	
	@Test
	public void testGetStatementBuilderFactoryforJoinSelectSingle() throws ODataException{
		
		GetEntityUriInfo getEntityView = getEntityUriInfo();
		
		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.JOIN_SINGLE, getEntityView).build();
		JPQLStatementBuilder statementBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getStatementBuilder(selectContext);
		
		assertTrue(statementBuilder instanceof JPQLJoinSelectSingleStatementBuilder);
				
	}
	
		
	@Test
	public void testGetContextBuilderforDelete() throws ODataException{
				
		// Build JPQL ContextBuilder
		JPQLContextBuilder  contextBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getContextBuilder(JPQLContextType.DELETE);
				
		assertNull(contextBuilder);
				
	}
	
	@Test
	public void testGetContextBuilderforSelect() throws ODataException{
				
		// Build JPQL ContextBuilder
		JPQLContextBuilder  contextBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getContextBuilder(JPQLContextType.SELECT);
				
		assertNotNull(contextBuilder);
		assertTrue(contextBuilder instanceof JPQLSelectContextBuilder);
				
	}
	
	@Test
	public void testGetContextBuilderforSelectSingle() throws ODataException{
				
		// Build JPQL ContextBuilder
		JPQLContextBuilder  contextBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getContextBuilder(JPQLContextType.SELECT_SINGLE);
				
		assertNotNull(contextBuilder);
		assertTrue(contextBuilder instanceof JPQLSelectSingleContextBuilder);
				
	}


	private GetEntitySetUriInfo getUriInfo() throws EdmException {
		GetEntitySetUriInfo getEntitySetView = EasyMock.createMock(GetEntitySetUriInfo.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		EasyMock.expect(getEntitySetView.getTargetEntitySet()).andStubReturn(edmEntitySet);
		EasyMock.expect(getEntitySetView.getOrderBy()).andStubReturn(orderByExpression);
		EasyMock.expect(getEntitySetView.getSelect()).andStubReturn(null);
		EasyMock.expect(getEntitySetView.getFilter()).andStubReturn(null);
		List<NavigationSegment> navigationSegments = new ArrayList<NavigationSegment>();
		EasyMock.expect(getEntitySetView.getNavigationSegments()).andStubReturn(navigationSegments);
		EasyMock.replay(getEntitySetView);
		EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
		EasyMock.replay(edmEntitySet);
		return getEntitySetView;
	}
	
	private GetEntityUriInfo getEntityUriInfo() throws EdmException {
		GetEntityUriInfo getEntityView = EasyMock.createMock(GetEntityUriInfo.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		EasyMock.expect(edmEntityType.getKeyProperties()).andStubReturn(new ArrayList<EdmProperty>());
		EasyMock.expect(edmEntityType.getName()).andStubReturn("");
		EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
		EasyMock.expect(getEntityView.getSelect()).andStubReturn(null);
		EasyMock.expect(getEntityView.getTargetEntitySet()).andStubReturn(edmEntitySet);
		EasyMock.replay(edmEntityType,edmEntitySet);
		EasyMock.expect(getEntityView.getKeyPredicates()).andStubReturn(new ArrayList<KeyPredicate>());
		List<NavigationSegment> navigationSegments = new ArrayList<NavigationSegment>();
		EasyMock.expect(getEntityView.getNavigationSegments()).andStubReturn(navigationSegments);
		EasyMock.replay(getEntityView);
		return getEntityView;
	}

	@Test
	public void testJPAAccessFactory(){
		ODataJPAFactoryImpl  oDataJPAFactoryImpl = new ODataJPAFactoryImpl();
		JPAAccessFactory jpaAccessFactory = oDataJPAFactoryImpl.getJPAAccessFactory();
		ODataJPAContextImpl oDataJPAContextImpl = new ODataJPAContextImpl();
		
		EntityManagerFactory emf = new EntityManagerFactory() {
			
			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Map<String, Object> getProperties() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public PersistenceUnitUtil getPersistenceUnitUtil() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Metamodel getMetamodel() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public CriteriaBuilder getCriteriaBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Cache getCache() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			public EntityManager createEntityManager(Map arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public EntityManager createEntityManager() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}
		};
		oDataJPAContextImpl.setEntityManagerFactory(emf );
		oDataJPAContextImpl.setPersistenceUnitName("pUnit");
		
		assertNotNull(jpaAccessFactory.getJPAProcessor(oDataJPAContextImpl));
		assertNotNull(jpaAccessFactory.getJPAEdmModelView(oDataJPAContextImpl));
		
	}

	@Test
	public void testOdataJpaAccessFactory(){

		ODataJPAFactoryImpl  oDataJPAFactoryImpl = new ODataJPAFactoryImpl();
		ODataJPAAccessFactory jpaAccessFactory = oDataJPAFactoryImpl.getODataJPAAccessFactory();
		ODataJPAContextImpl oDataJPAContextImpl = new ODataJPAContextImpl();
		
		EntityManagerFactory emf = new EntityManagerFactory() {
			
			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Map<String, Object> getProperties() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public PersistenceUnitUtil getPersistenceUnitUtil() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Metamodel getMetamodel() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public CriteriaBuilder getCriteriaBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Cache getCache() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			public EntityManager createEntityManager(Map arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public EntityManager createEntityManager() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}
		};
		oDataJPAContextImpl.setEntityManagerFactory(emf );
		oDataJPAContextImpl.setPersistenceUnitName("pUnit");
		
		assertNotNull(jpaAccessFactory.getODataJPAMessageService(new Locale("en")));
		assertNotNull(jpaAccessFactory.createODataJPAContext());
		assertNotNull(jpaAccessFactory.createJPAEdmProvider(oDataJPAContextImpl));
		assertNotNull(jpaAccessFactory.createODataProcessor(oDataJPAContextImpl));
		
	
	}
}
