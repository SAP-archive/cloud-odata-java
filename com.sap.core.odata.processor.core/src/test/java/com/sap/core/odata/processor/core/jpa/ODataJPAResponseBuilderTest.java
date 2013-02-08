package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmTestModelView;

public class ODataJPAResponseBuilderTest extends JPAEdmTestModelView{

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBuildListOfTGetEntitySetUriInfoStringODataJPAContext() {
		try {
			ODataJPAResponseBuilder.build(getJPAEntities(), getResultsView(), "application/xml", getODataJPAContext());
		} catch (ODataJPARuntimeException e) {
			fail("ODataJPARuntimeException not expected");
		}
	}
	
	@Test
	public void testBuildNegatives() {// Bad content type
		try {
			EntityType entity = new EntityType();
			entity.setName("SalesOrderHeader");
			ODataJPAResponseBuilder.build(entity, getLocalGetURIInfo(), "xml", getODataJPAContext());
		} catch (ODataJPARuntimeException e) {
			//Nothing to do, Expected.
		}
		
		
		try {// Bad content type
			ODataJPAResponseBuilder.build(getJPAEntities(), getResultsView(), "xml", getODataJPAContext());
		} catch (ODataJPARuntimeException e) {
			//Nothing to do, Expected.
		}
	}

	@Test
	public void testBuildObjectGetEntityUriInfoStringODataJPAContext() {
		try {
			EntityType entity = new EntityType();
			entity.setName("SalesOrderHeader");
			ODataJPAResponseBuilder.build(entity, getLocalGetURIInfo(), "application/xml", getODataJPAContext());
		} catch (ODataJPARuntimeException e) {
			e.printStackTrace();
			fail("ODataJPARuntimeException not expected");
		}
	}

	@Test
	public void testBuildNullSelects() {// Bad content type
		try {
			ODataJPAResponseBuilder.build(getJPAEntities(), getResultsViewWithNullSelects(), "xml", getODataJPAContext());
		} catch (ODataJPARuntimeException e) {
			//Nothing to do, Expected.
		}
	}
	
	private ODataJPAContext getODataJPAContext() {
		ODataJPAContext objODataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EasyMock.expect(objODataJPAContext.getODataContext()).andReturn(getLocalODataContext()).times(10);
		
		EasyMock.replay(objODataJPAContext);
		return objODataJPAContext;
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

	private GetEntitySetUriInfo getResultsView() {
		GetEntitySetUriInfo objGetEntitySetUriInfo = EasyMock.createMock(GetEntitySetUriInfo.class);
		EasyMock.expect(objGetEntitySetUriInfo.getInlineCount()).andReturn(getLocalInlineCount()).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getTargetEntitySet()).andReturn(getLocalTargetEntitySet()).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getSelect()).andReturn(getSelectItemList()).times(10);
		
		EasyMock.replay(objGetEntitySetUriInfo);
		return objGetEntitySetUriInfo;
	}
	
	private GetEntitySetUriInfo getResultsViewWithNullSelects() {
		GetEntitySetUriInfo objGetEntitySetUriInfo = EasyMock.createMock(GetEntitySetUriInfo.class);
		EasyMock.expect(objGetEntitySetUriInfo.getInlineCount()).andReturn(getLocalInlineCount()).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getTargetEntitySet()).andReturn(getLocalTargetEntitySet()).times(10);
		EasyMock.expect(objGetEntitySetUriInfo.getSelect()).andReturn(null).times(10);
		
		EasyMock.replay(objGetEntitySetUriInfo);
		return objGetEntitySetUriInfo;
	}
	
	private GetEntityUriInfo getLocalGetURIInfo() {
		GetEntityUriInfo objGetEntityUriInfo = EasyMock.createMock(GetEntityUriInfo.class);
		EasyMock.expect(objGetEntityUriInfo.getSelect()).andReturn(getSelectItemList()).times(10);
		EasyMock.expect(objGetEntityUriInfo.getTargetEntitySet()).andReturn(getLocalTargetEntitySet()).times(10);
		EasyMock.expect(objGetEntityUriInfo.getSelect()).andReturn(getSelectItemList()).times(10);
		
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
		EasyMock.expect(selectItem.getProperty()).andReturn(getEdmProperty()).times(10);
		EasyMock.replay(selectItem);
		return selectItem;
	}

	private EdmTyped getEdmProperty() {
		EdmProperty edmTyped = EasyMock.createMock(EdmProperty.class);
		
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andReturn(
				"Field1").times(10);
		EasyMock.replay(edmMapping);
		
		EdmType edmType = EasyMock.createMock(EdmType.class);
		
		try {
			EasyMock.expect(edmType.getKind()).andReturn(EdmTypeKind.SIMPLE).times(10);
			EasyMock.expect(edmType.getName()).andReturn("identifier").times(10);
			EasyMock.expect(edmTyped.getName()).andReturn("SalesOrderHeader").times(10);
			EasyMock.expect(edmTyped.getMapping())
					.andReturn(edmMapping).times(10);
			
			EasyMock.expect(edmTyped.getType()).andReturn(edmType).times(10);
			EasyMock.expect(edmTyped.getMapping()).andReturn(edmMapping).times(10);
			
		} catch (EdmException e) {
			fail("EdmException not expected");
		}
		EasyMock.replay(edmType);
		EasyMock.replay(edmTyped);
		return edmTyped;
	}

	private EdmEntitySet getLocalTargetEntitySet() {
		EdmEntitySet objEdmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		try {
			EasyMock.expect(objEdmEntitySet.getEntityType()).andReturn(getLocalEdmEntityType()).times(10);
			EasyMock.expect(objEdmEntitySet.getName()).andReturn("SalesOderHeaders").times(10);
			EasyMock.expect(objEdmEntitySet.getEntityContainer()).andReturn(getLocalEdmEntityContainer()).times(10);
		} catch (EdmException e) {
			fail("EdmException not expected");
		}
		
		EasyMock.replay(objEdmEntitySet);
		return objEdmEntitySet;
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

	private EdmEntityType getLocalEdmEntityType() {
		EdmEntityType objEdmEntityType = EasyMock.createMock(EdmEntityType.class);
		try {
			EasyMock.expect(objEdmEntityType.getName()).andReturn("SalesOderHeaders").times(10);
			EasyMock.expect(objEdmEntityType.getNamespace()).andReturn("SalesOderHeaders").times(10);
			EasyMock.expect(objEdmEntityType.hasStream()).andReturn(false).times(10);
			EasyMock.expect(objEdmEntityType.hasStream()).andReturn(false).times(10);
		    EasyMock.expect(objEdmEntityType.getPropertyNames()).andReturn(new ArrayList<String>()).times(10);
		    EasyMock.expect(objEdmEntityType.getNavigationPropertyNames()).andReturn(new ArrayList<String>()).times(10);
		    EasyMock.expect(objEdmEntityType.getKeyPropertyNames()).andReturn(new ArrayList<String>()).times(10);
		} catch (EdmException e) {
			fail("EdmException not expected");
		}
		EasyMock.replay(objEdmEntityType);
		return objEdmEntityType;
	}

	private InlineCount getLocalInlineCount() {
		return InlineCount.NONE;
	}

	private  List<Object> getJPAEntities() {
		List<Object> listJPAEntities = new ArrayList<Object>();
		EntityType entity = new EntityType();
		entity.setName("SalesOrderHeader");
		listJPAEntities.add(entity);
		return listJPAEntities;
	}

}
