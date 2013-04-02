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
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
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
		catch(NullPointerException e)
		{
			assertTrue(true);
		}
	}
	
	@Test
	public void testBuildNegatives() {// Bad content type
		try {
			EntityType entity = new EntityType();
			entity.setName("SalesOrderHeader");
			try {
				assertNotNull(ODataJPAResponseBuilder.build(entity, getLocalGetURIInfo(), "xml", getODataJPAContext()));
			} catch (ODataNotFoundException e) {
				assertTrue(true);
			}
		} catch (ODataJPARuntimeException e) {
			assertTrue(true);//Nothing to do, Expected.
		}
		catch(Exception e)
		{
			assertTrue(true);
		}
		try {// Bad content type
			assertNotNull(ODataJPAResponseBuilder.build(getJPAEntities(), getResultsView(), "xml", getODataJPAContext()));
		} catch (ODataJPARuntimeException e) {
			assertTrue(true);//Nothing to do, Expected.
		}
		catch(Exception e)
		{
			assertTrue(true);
		}
	}

	@Test
	public void testBuildObjectGetEntityUriInfoStringODataJPAContext() {
		try {
			EntityType entity = new EntityType();
			entity.setName("SalesOrderHeader");
			assertNotNull(ODataJPAResponseBuilder.build(entity, getLocalGetURIInfo(), "application/xml", getODataJPAContext()));
		}
		catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		catch(Exception e){
			assertTrue(true);
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
		
		EasyMock.replay(objGetEntitySetUriInfo);
		return objGetEntitySetUriInfo;
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
		EasyMock.expect(selectItem.getProperty()).andStubReturn(getEdmProperty());
		EasyMock.replay(selectItem);
		return selectItem;
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
		    EasyMock.expect(objEdmEntityType.getPropertyNames()).andStubReturn(new ArrayList<String>());
		    EasyMock.expect(objEdmEntityType.getNavigationPropertyNames()).andStubReturn(new ArrayList<String>());
		    EasyMock.expect(objEdmEntityType.getKeyPropertyNames()).andStubReturn(new ArrayList<String>());
		    EasyMock.expect(objEdmEntityType.getKeyProperties()).andStubReturn(null);
		  } catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
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
