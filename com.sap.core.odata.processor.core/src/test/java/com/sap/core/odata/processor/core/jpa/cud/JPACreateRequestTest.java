package com.sap.core.odata.processor.core.jpa.cud;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.core.ep.entry.EntryMetadataImpl;
import com.sap.core.odata.core.ep.entry.MediaMetadataImpl;
import com.sap.core.odata.core.ep.entry.ODataEntryImpl;
import com.sap.core.odata.core.uri.ExpandSelectTreeNodeImpl;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

public class JPACreateRequestTest {

	

	@Test
	public void testProcess() {
		
		JPACreateRequest createRequest = new JPACreateRequest(JPATestUtil.mockMetaModel());
		PostUriInfo postUriInfo = JPATestUtil.getPostUriInfo();
		try {
			createRequest.process(postUriInfo, null, "application/xml");
		} 
		catch (ODataJPARuntimeException e) {
			if(e.isCausedByMessageException())
				assertTrue(true);
			else
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
	}
	
	@Test
	public void testGetSetterName()
	{
		JPACreateRequest createRequest = new JPACreateRequest();
		Method method = getMethodForTesting("getSetterName",createRequest);
		if(method != null)
		{
			method.setAccessible(true);
			Object[] actualParams = {"salesOrderItems"};
			try {
				String  result = (String) method.invoke(createRequest, actualParams);
				assertEquals("setSalesOrderItems", result);
			} catch (IllegalArgumentException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (IllegalAccessException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (InvocationTargetException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			}
		}
		
	}

	@Test
	public void testParse2JPAEntityValueMap() {
		JPACreateRequest createRequest = new JPACreateRequest();
		EdmStructuralType edmEntityType = JPATestUtil.getEdmStructuralType();
		Object result = null;
		try {
			result = createRequest.parse2JPAEntityValueMap(JPATestUtil.getJPAEntity(), edmEntityType, JPATestUtil.getPropertyValueMap(), "SalesOrderHeader");
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		assertNotNull(result);
		assertEquals(((SalesOrderHeader)result).getId(), 1);
		
	}
	
	
	
	@Test
	public void testCreateInlinedEntities()
	{
		JPACreateRequest createRequest = new JPACreateRequest(JPATestUtil.mockMetaModel());
		Method method = getMethodForTesting("createInlinedEntities", createRequest);
		if(method != null)
		{
			method.setAccessible(true);
			EdmEntitySet edmEntitySet = JPATestUtil.mockSourceEdmEntitySet();
			ODataEntryImpl odataEntry = createODataEntry();
			Object[] actualParams = {JPATestUtil.getJPAEntity(),edmEntitySet,odataEntry,"SalesOrderHeader"};
			try {
				
				method.invoke(createRequest, actualParams);
				
			} catch (IllegalArgumentException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (IllegalAccessException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (InvocationTargetException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			}
			
		}
		
	}
	
	@Test
	public void testGetSettersForNavigationProperties()
	{
		JPACreateRequest createRequest = new JPACreateRequest();
		Method method = getMethodForTesting("getSettersForNavigationProperties", createRequest);
		if(method != null)
		{
			method.setAccessible(true);
			Map<String,Class<?>> relatedClassMap = new HashMap<String, Class<?>>();
			relatedClassMap.put("salesOrderLineItems", SalesOrderLineItem.class);
			Object[] actualParams = {JPATestUtil.getJPAEntity(),JPATestUtil.mockSourceEdmEntitySet(),relatedClassMap};
			try {
				@SuppressWarnings("unchecked")
				List<HashMap<?, ?>> result = (List<HashMap<?, ?>>) method.invoke(createRequest, actualParams);
				assertEquals(2, result.size());
			} catch (IllegalArgumentException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (IllegalAccessException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (InvocationTargetException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			}
			
		}
	}
	
	@Test
	public void getSetNavigationProperties()
	{
		JPACreateRequest createRequest = new JPACreateRequest();
		Method method = getMethodForTesting("setNavigationProperties", createRequest);
		
		if(method != null)
		{
			method.setAccessible(true);
			Map<String,Class<?>> relatedClassMap = new HashMap<String, Class<?>>();
			relatedClassMap.put("salesOrderLineItems", SalesOrderLineItem.class);
			Map<String,Object> propertyValueMap = new HashMap<String, Object>();
			propertyValueMap.put("id", 1);
			propertyValueMap.put("salesOrderLineItems", new SalesOrderLineItem(23));
			Object[] actualParams = {JPATestUtil.getJPAEntity(),JPATestUtil.mockSourceEdmEntitySet(),propertyValueMap,"SalesOrderHeader",relatedClassMap};
			try {
				method.invoke(createRequest, actualParams);
				//This method returns nothing. It only sets value in an object.
				// If no exception is thrown then we assert true
				assertTrue(true);
			} catch (IllegalArgumentException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (IllegalAccessException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (InvocationTargetException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			}
		}
	}
	
	private ODataEntryImpl createODataEntry() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		HashMap<String, Object> data1 = new HashMap<String, Object>();
		List<ODataEntry> oDataEntryList = new ArrayList<ODataEntry>();
		
		MediaMetadataImpl mediaMetadata = new MediaMetadataImpl();
		mediaMetadata.setContentType("application/xml");
		EntryMetadataImpl entryMetadata = new EntryMetadataImpl();
		entryMetadata.setId("http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/SalesOrders(5L)");
		ExpandSelectTreeNodeImpl  expandSelectTree = new ExpandSelectTreeNodeImpl();
		expandSelectTree.putLinkNode("SalesOrderLineItemDetails", new ExpandSelectTreeNodeImpl());
		expandSelectTree.setExpanded();
		expandSelectTree.setAllExplicitly();
		data.put("id", 1);
		data.put("SalesOrderLineItemDetails", new SalesOrderLineItem(23));
		ODataEntry lineItemOdataEntry = new ODataEntryImpl(data1, mediaMetadata, entryMetadata, expandSelectTree);
		oDataEntryList.add(lineItemOdataEntry);
		data.put("id", 1);
		data.put("SalesOrderLineItemDetails", oDataEntryList);
		ODataEntryImpl odataEntry = new ODataEntryImpl(data, mediaMetadata, entryMetadata, expandSelectTree);
		odataEntry.setContainsInlineEntry(true);
		return odataEntry;
	}


	private Method getMethodForTesting(String methodName,Object object)
	{
		Method method = null;
		for(Method m:object.getClass().getDeclaredMethods()){
			if(m.getName().equals(methodName))
			{
				method = m;
				break;
			}
		}
		return method;
	}
	

}
