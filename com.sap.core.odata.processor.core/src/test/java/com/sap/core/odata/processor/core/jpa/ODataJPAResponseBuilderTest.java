package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmTestModelView;

public class ODataJPAResponseBuilderTest extends JPAEdmTestModelView{

	@Test
	public void testBuildListOfTGetEntitySetUriInfoStringODataJPAContext() {
		try {
			assertNotNull(ODataJPAResponseBuilder.build(getJPAEntities(), getResultsView(), "application/xml", getODataJPAContext()));
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
	}
	
	@Test
	public void testBuildNegatives() {// Bad content type
		try {
			EntityType entity = new EntityType();
			entity.setName("SalesOrderHeader");
			try {
				assertNotNull(ODataJPAResponseBuilder.build(getEntity(), getLocalGetURIInfo(), "xml", getODataJPAContext()));
			} catch (ODataNotFoundException e) {
				assertTrue(true);
			}
		} catch (ODataJPARuntimeException e) {
			assertTrue(true);//Nothing to do, Expected.
		}
		try {// Bad content type
			assertNotNull(ODataJPAResponseBuilder.build(getJPAEntities(), getResultsView(), "xml", getODataJPAContext()));
		} catch (ODataJPARuntimeException e) {
			assertTrue(true);//Nothing to do, Expected.
		}
		
	}

	private Object getEntity() {
		SalesOrderHeader sHeader = new SalesOrderHeader(10, 34);
		return sHeader;
	}

	@Test
	public void testBuildObjectGetEntityUriInfoStringODataJPAContext() throws ODataNotFoundException {
		try {
			assertNotNull(ODataJPAResponseBuilder.build(new SalesOrderHeader(2, 10), getLocalGetURIInfo(), "application/xml", getODataJPAContext()));
		}
		catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	@Test
	public void testBuildNullSelects() {// Bad content type
		try {
			ODataJPAResponseBuilder.build(getJPAEntities(), getResultsViewWithNullSelects(), "xml", getODataJPAContext());
		} catch (ODataJPARuntimeException e) {
			assertTrue(true);//Nothing to do, Expected.
		}
		catch(Exception e)
		{
			assertTrue(true);
		}
	}
	
	@Test
	public void testBuildGetCount() {
		ODataResponse objODataResponse = null;
		try {
			objODataResponse = ODataJPAResponseBuilder.build(1, getCountEntitySetUriInfo(), "application/xml", getODataJPAContext());
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		assertNotNull(objODataResponse);
	}
	
	private GetEntitySetCountUriInfo getCountEntitySetUriInfo() {
		GetEntitySetCountUriInfo objGetEntitySetCountUriInfo = EasyMock.createMock(GetEntitySetCountUriInfo.class);
		EasyMock.replay(objGetEntitySetCountUriInfo);
		return objGetEntitySetCountUriInfo;
	}

	private ODataJPAContext getODataJPAContext() {
		ODataJPAContext objODataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EasyMock.expect(objODataJPAContext.getODataContext()).andStubReturn(getLocalODataContext());
		EasyMock.replay(objODataJPAContext);
		return objODataJPAContext;
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
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return uri;
	}

	private GetEntitySetUriInfo getResultsView() {
		GetEntitySetUriInfo objGetEntitySetUriInfo = EasyMock.createMock(GetEntitySetUriInfo.class);
		EasyMock.expect(objGetEntitySetUriInfo.getInlineCount()).andStubReturn(getLocalInlineCount());
		EasyMock.expect(objGetEntitySetUriInfo.getTargetEntitySet()).andStubReturn(getLocalTargetEntitySet());
		EasyMock.expect(objGetEntitySetUriInfo.getSelect()).andStubReturn(getSelectItemList());
		EasyMock.expect(objGetEntitySetUriInfo.getExpand()).andStubReturn(getExpandList());
		EasyMock.replay(objGetEntitySetUriInfo);
		return objGetEntitySetUriInfo;
	}
	
	private List<ArrayList<NavigationPropertySegment>> getExpandList() {
		List<ArrayList<NavigationPropertySegment>> expandList = new ArrayList<ArrayList<NavigationPropertySegment>>();
		/*ArrayList<NavigationPropertySegment> navSegments = new ArrayList<NavigationPropertySegment>();
		expandList.add(navSegments);
		NavigationPropertySegment navProp = EasyMock.createMock(NavigationPropertySegment.class);
		EasyMock.replay(navProp);*/
		return expandList;
	}

	private GetEntitySetUriInfo getResultsViewWithNullSelects() {
		GetEntitySetUriInfo objGetEntitySetUriInfo = EasyMock.createMock(GetEntitySetUriInfo.class);
		EasyMock.expect(objGetEntitySetUriInfo.getInlineCount()).andStubReturn(getLocalInlineCount());
		EasyMock.expect(objGetEntitySetUriInfo.getTargetEntitySet()).andStubReturn(getLocalTargetEntitySet());
		EasyMock.expect(objGetEntitySetUriInfo.getSelect()).andStubReturn(null);
		EasyMock.expect(objGetEntitySetUriInfo.getExpand()).andStubReturn(null);
		
		EasyMock.replay(objGetEntitySetUriInfo);
		return objGetEntitySetUriInfo;
	}
	
	private GetEntityUriInfo getLocalGetURIInfo() {
		GetEntityUriInfo objGetEntityUriInfo = EasyMock.createMock(GetEntityUriInfo.class);
		EasyMock.expect(objGetEntityUriInfo.getSelect()).andStubReturn(getSelectItemList());
		EasyMock.expect(objGetEntityUriInfo.getTargetEntitySet()).andStubReturn(getLocalTargetEntitySet());
		EasyMock.expect(objGetEntityUriInfo.getSelect()).andStubReturn(getSelectItemList());
		EasyMock.expect(objGetEntityUriInfo.getExpand()).andReturn(getExpandList());
		EasyMock.replay(objGetEntityUriInfo);
		return objGetEntityUriInfo;
	}


	private List<SelectItem> getSelectItemList() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		selectItems.add(getSelectItem());
		return selectItems;
	}

	private SelectItem getSelectItem() {
		SelectItem selectItem = EasyMock.createMock(SelectItem.class);
		EasyMock.expect(selectItem.getProperty()).andStubReturn(getEdmPropertyForSelect());
		List<NavigationPropertySegment> navigationSegmentList = new ArrayList<NavigationPropertySegment>();
		EasyMock.expect(selectItem.getNavigationPropertySegments()).andStubReturn(navigationSegmentList);
		EasyMock.replay(selectItem);
		return selectItem;
	}

	private EdmProperty getEdmPropertyForSelect() {
		EdmSimpleType edmType = EasyMock.createMock(EdmSimpleType.class);
		EasyMock.expect(edmType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
		Facets facets = new Facets().setNullable(false);
		try {
			EasyMock.expect(edmType.valueToString(new Integer(2), EdmLiteralKind.URI, facets)).andStubReturn("2");
			EasyMock.expect(edmType.valueToString(new Integer(2), EdmLiteralKind.DEFAULT, facets)).andStubReturn("2");
		} catch (EdmSimpleTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EasyMock.replay(edmType);
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn("soId");
		EasyMock.expect(edmMapping.getMimeType()).andReturn(null);
		EasyMock.replay(edmMapping);
		try {
			EasyMock.expect(edmProperty.getName()).andStubReturn("ID");
			EasyMock.expect(edmProperty.getType()).andStubReturn(edmType);
			EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
			EasyMock.expect(edmProperty.getFacets()).andStubReturn(facets);
			EasyMock.expect(edmProperty.getCustomizableFeedMappings()).andStubReturn(null);
			EasyMock.expect(edmProperty.getMimeType()).andStubReturn(null);
			EasyMock.replay(edmProperty);
			
		} catch (EdmException e) {
			fail("There is an exception is mocking some object "+e.getMessage());
		}
		
		return edmProperty;
		
		
	}

	private EdmProperty getEdmProperty() {
		EdmProperty edmTyped = EasyMock.createMock(EdmProperty.class);
		
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn(
				"Field1");
		EasyMock.replay(edmMapping);
		
		EdmType edmType = EasyMock.createMock(EdmType.class);
		
		try {
			EasyMock.expect(edmType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmType.getName()).andStubReturn("identifier");
			EasyMock.expect(edmTyped.getName()).andStubReturn("SalesOrderHeader");
			EasyMock.expect(edmTyped.getMapping())
					.andStubReturn(edmMapping);
			
			EasyMock.expect(edmTyped.getType()).andStubReturn(edmType);
			EasyMock.expect(edmTyped.getMapping()).andStubReturn(edmMapping);
			
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(edmType);
		EasyMock.replay(edmTyped);
		return edmTyped;
	}

	private EdmEntitySet getLocalTargetEntitySet() {
		EdmEntitySet objEdmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		try {
			EasyMock.expect(objEdmEntitySet.getEntityType()).andStubReturn(getLocalEdmEntityType());
			EasyMock.expect(objEdmEntitySet.getName()).andStubReturn("SalesOderHeaders");
			EasyMock.expect(objEdmEntitySet.getEntityContainer()).andStubReturn(getLocalEdmEntityContainer());
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
		EasyMock.replay(objEdmEntitySet);
		return objEdmEntitySet;
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

	private EdmEntityType getLocalEdmEntityType() {
		EdmEntityType objEdmEntityType = EasyMock.createMock(EdmEntityType.class);
		try {
			EasyMock.expect(objEdmEntityType.getName()).andStubReturn("SalesOderHeaders");
			EasyMock.expect(objEdmEntityType.getNamespace()).andStubReturn("SalesOderHeaders");
			EasyMock.expect(objEdmEntityType.hasStream()).andStubReturn(false);
			EasyMock.expect(objEdmEntityType.hasStream()).andStubReturn(false);
			ArrayList<String> propertyNames = new ArrayList<String>();
			propertyNames.add("ID");
			EasyMock.expect(objEdmEntityType.getProperty("ID")).andStubReturn(getEdmPropertyForSelect());
		    EasyMock.expect(objEdmEntityType.getPropertyNames()).andStubReturn(propertyNames);
		    EasyMock.expect(objEdmEntityType.getNavigationPropertyNames()).andStubReturn(new ArrayList<String>());
		    EasyMock.expect(objEdmEntityType.getKeyPropertyNames()).andStubReturn(propertyNames);
		    EasyMock.expect(objEdmEntityType.getKeyProperties()).andStubReturn(getKeyProperties());
		  } catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(objEdmEntityType);
		return objEdmEntityType;
	}

	

	private List<EdmProperty> getKeyProperties() {
		List<EdmProperty> edmProperties = new ArrayList<EdmProperty>();
		EdmType edmType = EasyMock.createMock(EdmType.class);
		EasyMock.expect(edmType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
		EasyMock.replay(edmType);
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn("soId");
		EasyMock.replay(edmMapping);
		try {
			EasyMock.expect(edmProperty.getName()).andStubReturn("ID");
			EasyMock.expect(edmProperty.getType()).andStubReturn(edmType);
			EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
			EasyMock.replay(edmProperty);
		} catch (EdmException e) {
			fail("There is an exception is mocking some object "+e.getMessage());
		}
		
		
		edmProperties.add(edmProperty);
		return edmProperties;
	}

	private InlineCount getLocalInlineCount() {
		return InlineCount.NONE;
	}

	class SalesOrderHeader
	{
		private int soId;
		private int Field1;
		public SalesOrderHeader(int soId,int field) {
			this.soId = soId;
			this.Field1 = field;
		}
		public int getField1() {
			return Field1;
		}
		public void setField1(int field1) {
			Field1 = field1;
		}
		public int getSoId() {
			return soId;
		}
		public void setSoId(int soId) {
			this.soId = soId;
		}
		
	}
	private  List<Object> getJPAEntities() {
		List<Object> listJPAEntities = new ArrayList<Object>();
		SalesOrderHeader entity;
		entity = new SalesOrderHeader(2,10);
		listJPAEntities.add(entity);
		return listJPAEntities;
	}

}
