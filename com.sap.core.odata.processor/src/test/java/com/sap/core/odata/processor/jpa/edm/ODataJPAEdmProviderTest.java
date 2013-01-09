package com.sap.core.odata.processor.jpa.edm;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.metamodel.EntityType;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
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
	public void testgetComplexType()
	{
		FullQualifiedName complexTypeName = new FullQualifiedName(MockData.NAME_SPACE, MockData.COMPLEX_TYPE_NAME);
		String nameStr = null;
		try {
			nameStr = edmProvider.getComplexType(complexTypeName).getName();
		} catch (ODataException e) {
			fail("Odata Exception raised with "+e.getMessage());
		}
		assertEquals(MockData.COMPLEX_TYPE_NAME, nameStr);
	}
	@Test
	public void testgetComplexTypeWithBuffer() throws Exception
	{
		HashMap<String, ComplexType> compTypes = new HashMap<String, ComplexType>();
		ComplexType comp = new ComplexType();
		comp.setName(MockData.COMPLEX_TYPE_NAME);
		compTypes.put(MockData.NAME_SPACE+"."+MockData.COMPLEX_TYPE_NAME, comp);
		ODataJPAEdmProvider jpaEdmProv = new ODataJPAEdmProvider();
		Class claz = jpaEdmProv.getClass();
		Field f = claz.getDeclaredField("complexTypes");
		f.setAccessible(true);
		f.set(jpaEdmProv, compTypes);
		assertEquals(comp, jpaEdmProv.getComplexType(new FullQualifiedName(MockData.NAME_SPACE, MockData.COMPLEX_TYPE_NAME)));
		try
		{
			jpaEdmProv.getComplexType(new FullQualifiedName(MockData.NAME_SPACE, "abc"));
		}
		catch(ODataJPAModelException e)
		{
			assertTrue(true);
		}
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
	public void testGetEntityContainerInfoWithBuffer() throws Exception
	{
		HashMap<String, EntityContainerInfo> entityContainerInfos = new HashMap<String, EntityContainerInfo>();
		EntityContainerInfo entityContainer = new EntityContainerInfo();
		entityContainer.setName(MockData.ENTITY_CONTAINER_NAME);
		entityContainerInfos.put(MockData.ENTITY_CONTAINER_NAME, entityContainer);
		ODataJPAEdmProvider jpaEdmProv = new ODataJPAEdmProvider();
		Class claz = jpaEdmProv.getClass();
		Field f = claz.getDeclaredField("entityContainerInfos");
		f.setAccessible(true);
		f.set(jpaEdmProv, entityContainerInfos);
		assertEquals(entityContainer, jpaEdmProv.getEntityContainerInfo(MockData.ENTITY_CONTAINER_NAME));
		try
		{
			jpaEdmProv.getEntityContainerInfo(MockData.ENTITY_CONTAINER_NAME);
		}
		catch(ODataJPAModelException e)
		{
			assertTrue(true);
		}
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
	public void testGetEntityTypeWithBuffer() throws Exception
	{
		HashMap<String, com.sap.core.odata.api.edm.provider.EntityType> entityTypes = new HashMap<String,com.sap.core.odata.api.edm.provider.EntityType >();
		com.sap.core.odata.api.edm.provider.EntityType entity = new com.sap.core.odata.api.edm.provider.EntityType();
		entity.setName(MockData.ENTITY_NAME_1);
		entityTypes.put(MockData.NAME_SPACE+"."+MockData.ENTITY_NAME_1, entity);
		ODataJPAEdmProvider jpaEdmProv = new ODataJPAEdmProvider();
		Class claz = jpaEdmProv.getClass();
		Field f = claz.getDeclaredField("entityTypes");
		f.setAccessible(true);
		f.set(jpaEdmProv, entityTypes);
		assertEquals(entity, jpaEdmProv.getEntityType(new FullQualifiedName(MockData.NAME_SPACE, MockData.ENTITY_NAME_1)));
		try
		{
			jpaEdmProv.getEntityType(new FullQualifiedName(MockData.NAME_SPACE, "abc"));
		}
		catch(ODataJPAModelException e)
		{
			assertTrue(true);
		}
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
