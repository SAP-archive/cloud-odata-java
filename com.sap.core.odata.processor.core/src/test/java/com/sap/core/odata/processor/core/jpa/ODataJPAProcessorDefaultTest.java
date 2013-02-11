package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.fail;

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
import com.sap.core.odata.processor.core.jpa.model.JPAEdmTestModelView;

public class ODataJPAProcessorDefaultTest extends JPAEdmTestModelView{

	ODataJPAProcessorDefault objODataJPAProcessorDefault;
	ODataJPAProcessorDefaultTest objODataJPAProcessorDefaultTest;
	@Before
	public void setUp() throws Exception {
		objODataJPAProcessorDefaultTest = new ODataJPAProcessorDefaultTest();
		objODataJPAProcessorDefault = new ODataJPAProcessorDefault(getLocalmockODataJPAContext());
	}
	
	
	@Test
	public void testReadEntitySetGetEntitySetUriInfoString() {
		try {
			GetEntityUriInfo getEntityView = getEntityUriInfo();
			Assert.assertNotNull(objODataJPAProcessorDefault.readEntity(getEntityView, "Application/xml"));
		} catch (ODataJPAModelException e1) {
		} catch (ODataJPARuntimeException e1) {//Expected
		} catch (ODataException e) {
		}
		
	}

	@Test
	public void testReadEntityGetEntityUriInfoString() {
		try {
			GetEntitySetUriInfo getEntityView = getEntitySetUriInfo();
			Assert.assertNotNull(objODataJPAProcessorDefault.readEntitySet(getEntityView, "Application/xml"));
		} catch (ODataJPAModelException e1) {
		} catch (ODataJPARuntimeException e1) {//Expected
		} catch (ODataException e) {
		}
	}

	private GetEntitySetUriInfo getEntitySetUriInfo() throws EdmException {
		GetEntitySetUriInfo objGetEntitySetUriInfo = EasyMock.createMock(GetEntitySetUriInfo.class);
		
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		EasyMock.expect(edmEntityType.getKeyProperties()).andReturn(
				new ArrayList<EdmProperty>()).times(10);
		EasyMock.expect(edmEntitySet.getEntityType()).andReturn(
				edmEntityType).times(10);
		EasyMock.expect(edmEntitySet.getName()).andReturn("SalesOrderHeaders").times(10);
		
		EasyMock.expect(objGetEntitySetUriInfo.getSelect()).andReturn(null).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getTargetEntitySet()).andReturn(
				edmEntitySet).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getOrderBy()).andReturn(getOrderByExpression()).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getTop()).andReturn(getTop()).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getSkip()).andReturn(getSkip()).times(10);
		
		EasyMock.expect(objGetEntitySetUriInfo.getInlineCount()).andReturn(getInlineCount()).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getFilter()).andReturn(getFilter()).times(10);
		EasyMock.expect(edmEntityType.getPropertyNames()).andReturn(getLocalPropertyNames()).times(10);
		EasyMock.expect(edmEntityType.getProperty("SoId")).andReturn(getEdmTypedMockedObj("SoId")).times(10);
		
		EasyMock.expect(edmEntityType.getKind()).andReturn(EdmTypeKind.SIMPLE).times(10);
		EasyMock.expect(edmEntityType.getNamespace()).andReturn("SalesOrderHeaders").times(10);
		EasyMock.expect(edmEntityType.getName()).andReturn("SalesOrderHeaders").times(10);
		EasyMock.expect(edmEntityType.hasStream()).andReturn(false).times(10);
	    EasyMock.expect(edmEntityType.getNavigationPropertyNames()).andReturn(new ArrayList<String>()).times(10);
	    EasyMock.expect(edmEntityType.getKeyPropertyNames()).andReturn(new ArrayList<String>()).times(10);
	    
	    EasyMock.expect(edmEntitySet.getEntityContainer()).andReturn(getLocalEdmEntityContainer()).times(10);
		
		EasyMock.replay(edmEntityType, edmEntitySet);
		
		EasyMock.expect(objGetEntitySetUriInfo.getStartEntitySet()).andReturn(edmEntitySet);
		
		EasyMock.replay(objGetEntitySetUriInfo);
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
		EasyMock.expect(odataJPAContext.getPersistenceUnitName()).andReturn("salesorderprocessing").times(2);
		EasyMock.expect(odataJPAContext.getEntityManagerFactory()).andReturn(mockEntityManagerFactory());
		EasyMock.expect(odataJPAContext.getODataContext()).andReturn(getLocalODataContext()).times(10);
		
		EasyMock.replay(odataJPAContext);
		return odataJPAContext;
	}

	private EntityManagerFactory mockEntityManagerFactory() {
		EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
		EasyMock.expect(emf.getMetamodel()).andReturn(mockMetaModel());
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
		EasyMock.expect(query.getResultList()).andReturn(getResultList()).times(10);
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

	private GetEntityUriInfo getEntityUriInfo() throws EdmException {
		GetEntityUriInfo getEntityView = EasyMock
				.createMock(GetEntityUriInfo.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		EasyMock.expect(edmEntityType.getKeyProperties()).andReturn(
				new ArrayList<EdmProperty>()).times(10);
		EasyMock.expect(edmEntitySet.getEntityType()).andReturn(
				edmEntityType).times(10);
		EasyMock.expect(edmEntitySet.getName()).andReturn("SalesOrderHeaders").times(10);
		
		EasyMock.expect(getEntityView.getSelect()).andReturn(null).times(10);
		EasyMock.expect(getEntityView.getTargetEntitySet()).andReturn(
				edmEntitySet).times(10);
		EasyMock.expect(edmEntityType.getPropertyNames()).andReturn(getLocalPropertyNames()).times(10);
		EasyMock.expect(edmEntityType.getProperty("SoId")).andReturn(getEdmTypedMockedObj("SoId")).times(10);
		
		EasyMock.expect(edmEntityType.getKind()).andReturn(EdmTypeKind.SIMPLE).times(10);
		EasyMock.expect(edmEntityType.getNamespace()).andReturn("SalesOrderHeaders").times(10);
		EasyMock.expect(edmEntityType.getName()).andReturn("SalesOrderHeaders").times(10);
		EasyMock.expect(edmEntityType.hasStream()).andReturn(false).times(10);
	    EasyMock.expect(edmEntityType.getNavigationPropertyNames()).andReturn(new ArrayList<String>()).times(10);
	    EasyMock.expect(edmEntityType.getKeyPropertyNames()).andReturn(new ArrayList<String>()).times(10);
	    
	    EasyMock.expect(edmEntitySet.getEntityContainer()).andReturn(getLocalEdmEntityContainer()).times(10);
		
		EasyMock.replay(edmEntityType, edmEntitySet);
		EasyMock.expect(getEntityView.getKeyPredicates()).andReturn(
				new ArrayList<KeyPredicate>()).times(10);
		List<NavigationSegment> navigationSegments = new ArrayList<NavigationSegment>();
		EasyMock.expect(getEntityView.getNavigationSegments()).andReturn(
				navigationSegments);
		EasyMock.expect(getEntityView.getStartEntitySet()).andReturn(edmEntitySet);
		
		EasyMock.replay(getEntityView);
		return getEntityView;
	}
	
	private EdmEntityContainer getLocalEdmEntityContainer() {
		EdmEntityContainer edmEntityContainer = EasyMock.createMock(EdmEntityContainer.class);
		EasyMock.expect(edmEntityContainer.isDefaultEntityContainer()).andReturn(true).times(10);
		try {
			EasyMock.expect(edmEntityContainer.getName()).andReturn("salesorderprocessingContainer").times(10);
		} catch (EdmException e) {
			fail("EdmException not expected");
		}
		
		EasyMock.replay(edmEntityContainer);
		return edmEntityContainer;
	}

	private EdmTyped getEdmTypedMockedObj(String propertyName)
			throws EdmException {
		EdmProperty mockedEdmProperty = EasyMock.createMock(EdmProperty.class);
		EasyMock.expect(mockedEdmProperty.getMapping())
				.andReturn(getEdmMappingMockedObj(propertyName)).times(10);
		EdmType edmType = EasyMock.createMock(EdmType.class);
		EasyMock.expect(edmType.getKind()).andReturn(EdmTypeKind.SIMPLE).times(10);
		EasyMock.replay(edmType);
		EasyMock.expect(mockedEdmProperty.getName()).andReturn("identifier").times(10);
		EasyMock.expect(mockedEdmProperty.getType()).andReturn(edmType).times(10);
		EasyMock.expect(mockedEdmProperty.getFacets()).andReturn(getEdmFacetsMockedObj()).times(10);
		
		EasyMock.replay(mockedEdmProperty);
		return mockedEdmProperty;
	}
	
	private EdmFacets getEdmFacetsMockedObj() {
		EdmFacets facets = EasyMock.createMock(EdmFacets.class);
		EasyMock.expect(facets.getConcurrencyMode()).andReturn(EdmConcurrencyMode.Fixed).times(10);

		EasyMock.replay(facets);
		return facets;
	}
	
	private EdmMapping getEdmMappingMockedObj(String propertyName) {
		EdmMapping mockedEdmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(mockedEdmMapping.getInternalName())
				.andReturn(propertyName).times(10);
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
			EasyMock.expect(objODataContext.getPathInfo()).andReturn(getLocalPathInfo()).times(10);
		} catch (ODataException e) {
			fail("ODataException not expected");
		}
		EasyMock.replay(objODataContext);
		return objODataContext;
	}
	
	private PathInfo getLocalPathInfo() {
		PathInfo pathInfo = EasyMock.createMock(PathInfo.class);
		EasyMock.expect(pathInfo.getServiceRoot()).andReturn(getLocalURI()).times(10);
		EasyMock.replay(pathInfo);
		return pathInfo;
	}
	
	private URI getLocalURI() {
		URI uri = null;
		try {
			uri = new URI("http://localhost:8080/com.sap.core.odata.processor.ref.web/");
		} catch (URISyntaxException e) {
			fail("URISyntaxException not expected");
		}
		return uri;
	}


}
