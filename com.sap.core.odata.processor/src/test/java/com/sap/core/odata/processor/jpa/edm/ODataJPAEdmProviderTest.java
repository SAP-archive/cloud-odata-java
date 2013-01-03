package com.sap.core.odata.processor.jpa.edm;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.jpa.util.Mock;
import com.sap.core.odata.processor.jpa.util.MockData;

public class ODataJPAEdmProviderTest {
	
	private static ODataJPAEdmProvider edmProvider;
	@BeforeClass
	  public static void setup() throws Exception {
		
		edmProvider = new ODataJPAEdmProvider();
		Class<? extends ODataJPAEdmProvider> clazz = edmProvider.getClass();
		Field field = clazz.getDeclaredField("builder");
		field.setAccessible(true);
		field.set(edmProvider,Mock.mockjpaEdmBuilder());
		field = clazz.getDeclaredField("oDataJPAContext");
		field.setAccessible(true);
		field.set(edmProvider, Mock.mockODataJPAContext());
		
	  }


	

	@Test
	public void testGetODataJPAContext() {
		String pUnitName= edmProvider.getODataJPAContext().getPersistenceUnitName();
		assertEquals(MockData.NAME_SPACE, pUnitName);
		
	}

	

	@Test
	public void testGetEntityContainerInfo() {
		String nameStr = null;
		try {
			nameStr = edmProvider.getEntityContainerInfo(MockData.ENTITY_CONTAINER_NAME).getName();
			} catch (ODataException e) {
			fail("OData Exception raised with "+e.getMessage());
		}
		
		assertEquals(MockData.ENTITY_CONTAINER_NAME, nameStr);
	}

	@Test
	public void testGetEntityType() {
		FullQualifiedName entityTypeName = new FullQualifiedName(MockData.NAME_SPACE, MockData.ENTITY_NAME_2);
		String entityName = null;
		try {
			entityName = edmProvider.getEntityType(entityTypeName).getName();
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertEquals(MockData.ENTITY_NAME_2, entityName);
	}

	@Test
	public void testGetEntitySet() {
		String entitySetName = null;
		try {
			entitySetName = edmProvider.getEntitySet(MockData.ENTITY_CONTAINER_NAME, MockData.ENTITY_NAME_1+"s").getName();
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertEquals(MockData.ENTITY_NAME_1+"s", entitySetName);
	}

	@Test
	public void testGetSchemas() {
		List<Schema> schemas = new ArrayList<Schema>();
		try {
			schemas = edmProvider.getSchemas();
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertEquals(MockData.NAME_SPACE, schemas.get(0).getNamespace());
	}

}
