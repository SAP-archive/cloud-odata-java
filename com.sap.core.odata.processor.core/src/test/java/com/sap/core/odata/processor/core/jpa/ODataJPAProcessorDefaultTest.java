package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.metamodel.Metamodel;

import org.easymock.EasyMock;
import org.junit.Assert;
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
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmTestModelView;

public class ODataJPAProcessorDefaultTest extends JPAEdmTestModelView{

	ODataJPAProcessorDefault objODataJPAProcessorDefault;
	ODataJPAProcessorDefaultTest objODataJPAProcessorDefaultTest;
	@Before
	public void setUp() {
		objODataJPAProcessorDefaultTest = new ODataJPAProcessorDefaultTest();
		objODataJPAProcessorDefault = new ODataJPAProcessorDefault(getLocalmockODataJPAContext());
	}
	
	
	@Test
	public void testReadEntitySetGetEntitySetUriInfoString() {
		try {
			GetEntityUriInfo getEntityView = getEntityUriInfo();
			Assert.assertNotNull(objODataJPAProcessorDefault.readEntity(getEntityView, "Application/xml"));
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e1) {//Expected
			assertTrue(true);
		} catch (ODataException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
	}

	@Test
	public void testReadEntityGetEntityUriInfoString() {
		try {
			GetEntitySetUriInfo getEntityView = getEntitySetUriInfo();
			Assert.assertNotNull(objODataJPAProcessorDefault.readEntitySet(getEntityView, "Application/xml"));
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e1) {//Expected
			assertTrue(true);
		} catch (ODataException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	private GetEntitySetUriInfo getEntitySetUriInfo() {
		GetEntitySetUriInfo objGetEntitySetUriInfo = EasyMock.createMock(GetEntitySetUriInfo.class);
		
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		try {
			EasyMock.expect(edmEntityType.getKeyProperties()).andStubReturn(
					new ArrayList<EdmProperty>());
			EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(
					edmEntityType);
			EasyMock.expect(edmEntitySet.getName()).andStubReturn("SalesOrderHeaders");
			
			EasyMock.expect(objGetEntitySetUriInfo.getSelect()).andStubReturn(null);
			EasyMock.expect(objGetEntitySetUriInfo.getTargetEntitySet()).andStubReturn(
					edmEntitySet);
			EasyMock.expect(objGetEntitySetUriInfo.getOrderBy()).andStubReturn(getOrderByExpression());
			EasyMock.expect(objGetEntitySetUriInfo.getTop()).andStubReturn(getTop());
			EasyMock.expect(objGetEntitySetUriInfo.getSkip()).andStubReturn(getSkip());
			
			EasyMock.expect(objGetEntitySetUriInfo.getInlineCount()).andStubReturn(getInlineCount());
			EasyMock.expect(objGetEntitySetUriInfo.getFilter()).andStubReturn(getFilter());
			EasyMock.expect(edmEntityType.getPropertyNames()).andStubReturn(getLocalPropertyNames());
			EasyMock.expect(edmEntityType.getProperty("SoId")).andStubReturn(getEdmTypedMockedObj("SoId"));
			
			EasyMock.expect(edmEntityType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmEntityType.getNamespace()).andStubReturn("SalesOrderHeaders");
			EasyMock.expect(edmEntityType.getName()).andStubReturn("SalesOrderHeaders");
			EasyMock.expect(edmEntityType.hasStream()).andStubReturn(false);
		    EasyMock.expect(edmEntityType.getNavigationPropertyNames()).andStubReturn(new ArrayList<String>());
		    EasyMock.expect(edmEntityType.getKeyPropertyNames()).andStubReturn(new ArrayList<String>());
	    
			EasyMock.expect(edmEntitySet.getEntityContainer()).andStubReturn(getLocalEdmEntityContainer());
			EasyMock.replay(edmEntityType, edmEntitySet);
			
			EasyMock.expect(objGetEntitySetUriInfo.getStartEntitySet()).andStubReturn(edmEntitySet);
			
			EasyMock.replay(objGetEntitySetUriInfo);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
		return objGetEntitySetUriInfo;
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
		EasyMock.replay(em);
		return em;
	}

	private Query getQuery() {
		Query query = EasyMock.createMock(Query.class);
		EasyMock.expect(query.getResultList()).andStubReturn(getResultList());
		EasyMock.replay(query);
		return query;
	}

	private List<?> getResultList() {
		List<Object> list = new ArrayList<Object>();
		list.add(new Address());
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

	private GetEntityUriInfo getEntityUriInfo() {
		GetEntityUriInfo getEntityView = EasyMock
				.createMock(GetEntityUriInfo.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		try {
			EasyMock.expect(edmEntityType.getKeyProperties()).andStubReturn(
					new ArrayList<EdmProperty>());
			EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(
					edmEntityType);
			EasyMock.expect(edmEntitySet.getName()).andStubReturn("SalesOrderHeaders");
			
			EasyMock.expect(getEntityView.getSelect()).andStubReturn(null);
			EasyMock.expect(getEntityView.getTargetEntitySet()).andStubReturn(
					edmEntitySet);
			EasyMock.expect(edmEntityType.getPropertyNames()).andStubReturn(getLocalPropertyNames());
			EasyMock.expect(edmEntityType.getProperty("SoId")).andStubReturn(getEdmTypedMockedObj("SoId"));
			
			EasyMock.expect(edmEntityType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmEntityType.getNamespace()).andStubReturn("SalesOrderHeaders");
			EasyMock.expect(edmEntityType.getName()).andStubReturn("SalesOrderHeaders");
			EasyMock.expect(edmEntityType.hasStream()).andStubReturn(false);
		    EasyMock.expect(edmEntityType.getNavigationPropertyNames()).andStubReturn(new ArrayList<String>());
		    EasyMock.expect(edmEntityType.getKeyPropertyNames()).andStubReturn(new ArrayList<String>());
		    
		    EasyMock.expect(edmEntitySet.getEntityContainer()).andStubReturn(getLocalEdmEntityContainer());
			
			EasyMock.replay(edmEntityType, edmEntitySet);
			EasyMock.expect(getEntityView.getKeyPredicates()).andStubReturn(
					new ArrayList<KeyPredicate>());
			List<NavigationSegment> navigationSegments = new ArrayList<NavigationSegment>();
			EasyMock.expect(getEntityView.getNavigationSegments()).andReturn(
					navigationSegments);
			EasyMock.expect(getEntityView.getStartEntitySet()).andReturn(edmEntitySet);
			
			EasyMock.replay(getEntityView);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return getEntityView;
	}
	
	private EdmEntityContainer getLocalEdmEntityContainer() {
		EdmEntityContainer edmEntityContainer = EasyMock.createMock(EdmEntityContainer.class);
		EasyMock.expect(edmEntityContainer.isDefaultEntityContainer()).andStubReturn(true);
		try {
			EasyMock.expect(edmEntityContainer.getName()).andStubReturn("salesorderprocessingContainer");
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
		EasyMock.expect(mockedEdmMapping.getInternalName())
				.andStubReturn(propertyName);
		EasyMock.replay(mockedEdmMapping);
		return mockedEdmMapping;
	}

	private List<String> getLocalPropertyNames() {
		List<String> list = new ArrayList<String>();
		list.add("SoId");
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
			uri = new URI("http://localhost:8080/com.sap.core.odata.processor.ref.web/");
		} catch (URISyntaxException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return uri;
	}


}
