package com.sap.core.odata.processor.core.jpa.access.data;

import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.metamodel.Metamodel;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

public class JPAProcessorImplTest {

	// -------------------------------- Common Start ------------------------------------common in DataJPAProcessorDefaultTest as well
	private static final String STR_LOCAL_URI = "http://localhost:8080/com.sap.core.odata.processor.ref.web/";
	private static final String SALESORDERPROCESSING_CONTAINER = "salesorderprocessingContainer";
	private static final String SO_ID = "SoId";
	private static final String SALES_ORDER = "SalesOrder";
	private static final String SALES_ORDER_HEADERS = "SalesOrderHeaders";
	//-------------------------------- Common End ------------------------------------
	
	JPAProcessorImpl objJPAProcessorImpl;
	@Before
	public void setUp() throws Exception {
		objJPAProcessorImpl = new JPAProcessorImpl(getLocalmockODataJPAContext());
	}

	@Test
	public void testProcessGetEntitySetCountUriInfo() {
		try {
			Assert.assertEquals(11, objJPAProcessorImpl.process(getEntitySetCountUriInfo()));
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}
	
	
	// ---------------------------- Common Code Start ---------------- TODO - common in DataJPAProcessorDefaultTest as well 
	private GetEntitySetCountUriInfo getEntitySetCountUriInfo() {
		return getLocalUriInfo();
	}
	

	/**
	 * @return
	 */
	private UriInfo getLocalUriInfo() {
		UriInfo objUriInfo = EasyMock.createMock(UriInfo.class);
		EasyMock.expect(objUriInfo.getStartEntitySet()).andStubReturn(getLocalEdmEntitySet());
		EasyMock.expect(objUriInfo.getTargetEntitySet()).andStubReturn(getLocalEdmEntitySet());
		EasyMock.expect(objUriInfo.getSelect()).andStubReturn(null);
		EasyMock.expect(objUriInfo.getOrderBy()).andStubReturn(getOrderByExpression());
		EasyMock.expect(objUriInfo.getTop()).andStubReturn(getTop());
		EasyMock.expect(objUriInfo.getSkip()).andStubReturn(getSkip());
		EasyMock.expect(objUriInfo.getInlineCount()).andStubReturn(getInlineCount());
		EasyMock.expect(objUriInfo.getFilter()).andStubReturn(getFilter());
		EasyMock.replay(objUriInfo);
		return objUriInfo;
	}


	/**
	 * @return
	 * @throws EdmException
	 */
	private EdmEntitySet getLocalEdmEntitySet() {
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		try {
			EasyMock.expect(edmEntitySet.getName()).andStubReturn(SALES_ORDER_HEADERS);
			EasyMock.expect(edmEntitySet.getEntityContainer()).andStubReturn(getLocalEdmEntityContainer());
			EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(getLocalEdmEntityType());
			EasyMock.replay(edmEntitySet);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return edmEntitySet;
	}


	/**
	 * @return
	 * @throws EdmException
	 */
	private EdmEntityType getLocalEdmEntityType() {
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		try {
			EasyMock.expect(edmEntityType.getKeyProperties()).andStubReturn(new ArrayList<EdmProperty>());
			EasyMock.expect(edmEntityType.getPropertyNames()).andStubReturn(getLocalPropertyNames());
			EasyMock.expect(edmEntityType.getProperty(SO_ID)).andStubReturn(getEdmTypedMockedObj(SALES_ORDER));
			EasyMock.expect(edmEntityType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmEntityType.getNamespace()).andStubReturn(SALES_ORDER_HEADERS);
			EasyMock.expect(edmEntityType.getName()).andStubReturn(SALES_ORDER_HEADERS);
			EasyMock.expect(edmEntityType.hasStream()).andStubReturn(false);
			EasyMock.expect(edmEntityType.getNavigationPropertyNames()).andStubReturn(new ArrayList<String>());
			EasyMock.expect(edmEntityType.getKeyPropertyNames()).andStubReturn(new ArrayList<String>());
			EasyMock.expect(edmEntityType.getMapping()).andStubReturn(getEdmMappingMockedObj(SALES_ORDER));// ID vs Salesorder ID
			EasyMock.replay(edmEntityType);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return edmEntityType;
	}


	private InlineCount getInlineCount() {
		return InlineCount.NONE;
	}


	private FilterExpression getFilter() {
		return null;
	}


	private Integer getSkip() {
		return null;
	}


	private Integer getTop() {
		return null;
	}


	private OrderByExpression getOrderByExpression() {
		return null;
	}


	private ODataJPAContext getLocalmockODataJPAContext() {
		ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EasyMock.expect(odataJPAContext.getPersistenceUnitName()).andStubReturn("salesorderprocessing");
		EasyMock.expect(odataJPAContext.getEntityManagerFactory()).andStubReturn(mockEntityManagerFactory());
		EasyMock.expect(odataJPAContext.getODataContext()).andStubReturn(getLocalODataContext());
		
		EasyMock.replay(odataJPAContext);
		return odataJPAContext;
	}

	private EntityManagerFactory mockEntityManagerFactory() {
		EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
		EasyMock.expect(emf.getMetamodel()).andStubReturn(mockMetaModel());
		EasyMock.expect(emf.createEntityManager()).andReturn(getLocalEntityManager());
		EasyMock.replay(emf);
		return emf;
	}

	private EntityManager getLocalEntityManager() {
		EntityManager em = EasyMock.createMock(EntityManager.class);
		EasyMock.expect(em.createQuery("SELECT E1 FROM SalesOrderHeaders E1")).andReturn(getQuery());
		EasyMock.expect(em.createQuery("SELECT COUNT ( E1 ) FROM SalesOrderHeaders E1")).andStubReturn(getQueryForSelectCount());
		EasyMock.replay(em);
		return em;
	}

	private Query getQuery() {
		Query query = EasyMock.createMock(Query.class);
		EasyMock.expect(query.getResultList()).andStubReturn(getResultList());
		EasyMock.replay(query);
		return query;
	}
	
	private Query getQueryForSelectCount() {
		Query query = EasyMock.createMock(Query.class);
		EasyMock.expect(query.getResultList()).andStubReturn(getResultListForSelectCount());
		EasyMock.replay(query);
		return query;
	}

	private List<?> getResultList() {
		List<Object> list = new ArrayList<Object>();
		list.add(new Address());
		return list;
	}
	
	private List<?> getResultListForSelectCount() {
		List<Object> list = new ArrayList<Object>();
		list.add(new Long(11));
		return list;
	}
	
	private class Address{
		@SuppressWarnings("unused")
		public String getSoId(){
			return "12";
		}
	}

	private Metamodel mockMetaModel() {
		Metamodel metaModel = EasyMock.createMock(Metamodel.class);
		EasyMock.replay(metaModel);
		return metaModel;
	}

	private EdmEntityContainer getLocalEdmEntityContainer() {
		EdmEntityContainer edmEntityContainer = EasyMock.createMock(EdmEntityContainer.class);
		EasyMock.expect(edmEntityContainer.isDefaultEntityContainer()).andStubReturn(true);
		try {
			EasyMock.expect(edmEntityContainer.getName()).andStubReturn(SALESORDERPROCESSING_CONTAINER);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
		EasyMock.replay(edmEntityContainer);
		return edmEntityContainer;
	}

	private EdmTyped getEdmTypedMockedObj(String propertyName){
		EdmProperty mockedEdmProperty = EasyMock.createMock(EdmProperty.class);
		try {
			EasyMock.expect(mockedEdmProperty.getMapping())
					.andStubReturn(getEdmMappingMockedObj(propertyName));
			EdmType edmType = EasyMock.createMock(EdmType.class);
			EasyMock.expect(edmType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.replay(edmType);
			EasyMock.expect(mockedEdmProperty.getName()).andStubReturn("identifier");
			EasyMock.expect(mockedEdmProperty.getType()).andStubReturn(edmType);
			EasyMock.expect(mockedEdmProperty.getFacets()).andStubReturn(getEdmFacetsMockedObj());
			
			EasyMock.replay(mockedEdmProperty);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return mockedEdmProperty;
	}
	
	private EdmFacets getEdmFacetsMockedObj() {
		EdmFacets facets = EasyMock.createMock(EdmFacets.class);
		EasyMock.expect(facets.getConcurrencyMode()).andStubReturn(EdmConcurrencyMode.Fixed);

		EasyMock.replay(facets);
		return facets;
	}
	
	private EdmMapping getEdmMappingMockedObj(String propertyName) {
		EdmMapping mockedEdmMapping = EasyMock.createMock(EdmMapping.class);
		if(propertyName.equalsIgnoreCase(SALES_ORDER)){
			EasyMock.expect(mockedEdmMapping.getInternalName())
			.andStubReturn(SALES_ORDER_HEADERS);
		}else{
			EasyMock.expect(mockedEdmMapping.getInternalName())
			.andStubReturn(propertyName);
		}
		EasyMock.replay(mockedEdmMapping);
		return mockedEdmMapping;
	}

	private List<String> getLocalPropertyNames() {
		List<String> list = new ArrayList<String>();
		list.add(SO_ID);
		return list;
	}
	
	private ODataContext getLocalODataContext() {
		ODataContext objODataContext = EasyMock.createMock(ODataContext.class);
		try {
			EasyMock.expect(objODataContext.getPathInfo()).andStubReturn(getLocalPathInfo());
		} catch (ODataException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(objODataContext);
		return objODataContext;
	}
	
	private PathInfo getLocalPathInfo() {
		PathInfo pathInfo = EasyMock.createMock(PathInfo.class);
		EasyMock.expect(pathInfo.getServiceRoot()).andStubReturn(getLocalURI());
		EasyMock.replay(pathInfo);
		return pathInfo;
	}
	
	private URI getLocalURI() {
		URI uri = null;
		try {
			uri = new URI(STR_LOCAL_URI);
		} catch (URISyntaxException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return uri;
	}

	
	//-------------------------------- Common End ------------------------------------

}
