package com.sap.core.odata.processor.core.jpa.edm;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.core.jpa.edm.ODataJPAEdmProvider;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmModel;
import com.sap.core.odata.processor.core.jpa.util.MockEdmSchema;
import com.sap.core.odata.processor.core.jpa.util.MockODataJPAContext;

public class ODataJPAEdmProviderTest {

	private static ODataJPAEdmProvider edmProvider;
	
	@BeforeClass
	  public static void setup() throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException  {
		
		edmProvider = new ODataJPAEdmProvider();
		Class<? extends ODataJPAEdmProvider> clazz = edmProvider.getClass();
		Field field = clazz.getDeclaredField("schemas");
		field.setAccessible(true);
		List<Schema> schemas = new ArrayList<Schema>();
		schemas.add(MockEdmSchema.createMockEdmSchema());
		field.set(edmProvider,schemas);
		field = clazz.getDeclaredField("oDataJPAContext");
		field.setAccessible(true);
		field.set(edmProvider, MockODataJPAContext.mockODataJPAContext());
		field = clazz.getDeclaredField("jpaEdmModel");
		field.setAccessible(true);
		field.set(edmProvider, new JPAEdmModel(null, null));
		
	  }
	@Test
	public void testConstructor()
	{
		try
		{
			ODataJPAEdmProvider edmProv = new ODataJPAEdmProvider(MockODataJPAContext.mockODataJPAContext());
			edmProv.getClass();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			assertTrue(true);
		}
	}

	@Test
	public void testGetODataJPAContext() {
		String pUnitName= edmProvider.getODataJPAContext().getPersistenceUnitName();
		assertEquals("salesorderprocessing", pUnitName);
	}

	@Test
	public void testGetEntityContainerInfo() {
		String entityContainerName = null;
		EntityContainerInfo entityContainer = null;
		try {
			entityContainer = edmProvider.getEntityContainerInfo("salesorderprocessingContainer");
			entityContainerName = entityContainer.getName();
			} catch (ODataException e) {
			fail("OData Exception raised with "+e.getMessage());
		}
		
		assertEquals("salesorderprocessingContainer", entityContainerName);
		assertNotNull(entityContainer);
	}

	@Test
	public void testGetEntityType() {
		FullQualifiedName entityTypeName = new FullQualifiedName("salesorderprocessing", "SalesOrderHeader");
		String entityName = null;
		try {
			entityName = edmProvider.getEntityType(entityTypeName).getName();
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertEquals("SalesOrderHeader", entityName);
		try{
			edmProvider.getEntityType(new FullQualifiedName("salesorder", "abc"));
		}
		catch (ODataException e) {
			assertTrue(true);
		}
		
	}

	@Test
	public void testGetComplexType() {
		FullQualifiedName complexTypeName = new FullQualifiedName("salesorderprocessing", "Address");
		String nameStr = null;
		try {
			nameStr = edmProvider.getComplexType(complexTypeName).getName();
		} catch (ODataException e) {
			fail("Odata Exception raised with "+e.getMessage());
		}
		assertEquals("Address", nameStr);
	}

	@Test
	public void testGetAssociationFullQualifiedName() {
		Association association = null;
		try {
			association = edmProvider.getAssociation(new FullQualifiedName("salesorderprocessing", "SalesOrderHeader_SalesOrderItem"));
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertNotNull(association);
		assertEquals("SalesOrderHeader_SalesOrderItem", association.getName());
	}

	@Test
	public void testGetEntitySet() {
		String entitySetName = null;
		try {
			entitySetName = edmProvider.getEntitySet("salesorderprocessingContainer", "SalesOrderHeaders").getName();
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertEquals("SalesOrderHeaders", entitySetName);
		try {
			entitySetName = edmProvider.getEntitySet("salesorderprocessing", "SalesOrderHeaders").getName();
		} catch (ODataException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAssociationSet() {
		AssociationSet associationSet = null;
		
		try {
			associationSet = edmProvider.getAssociationSet("salesorderprocessingContainer", new FullQualifiedName("salesorderprocessing", "SalesOrderHeader_SalesOrderItem"), "SalesOrderHeaders", "SalesOrderHeader");
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertNotNull(associationSet);
		assertEquals("SalesOrderHeader_SalesOrderItemSet", associationSet.getName());
		try {
			associationSet = edmProvider.getAssociationSet("salesorderprocessingContainer", new FullQualifiedName("salesorderprocessing", "SalesOrderHeader_SalesOrderItem"), "SalesOrderItems", "SalesOrderItem");
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
		assertNotNull(associationSet);
		try {
			associationSet = edmProvider.getAssociationSet("salesorderproceContainer", new FullQualifiedName("salesorderprocessing", "SalesOrderHeader_SalesOrderItem"), "SalesOrderItems", "SalesOrderItem");
		} catch (ODataException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetFunctionImport() {
		try {
			edmProvider.getFunctionImport("salesorderprocessingContainer", "abc");
		} catch (ODataException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetSchemas() {
		try {
			assertNotNull(edmProvider.getSchemas());
		} catch (ODataException e) {
			fail("OData exception raised with " + e.getMessage());
		}
	}
	@Test
	public void testgetComplexTypeWithBuffer() throws Exception
	{
		HashMap<String, ComplexType> compTypes = new HashMap<String, ComplexType>();
		ComplexType comp = new ComplexType();
		comp.setName("Address");
		compTypes.put("salesorderprocessing"+"."+"Address", comp);
		ODataJPAEdmProvider jpaEdmProv = new ODataJPAEdmProvider();
		Class<?> claz = jpaEdmProv.getClass();
		Field f = claz.getDeclaredField("complexTypes");
		f.setAccessible(true);
		f.set(jpaEdmProv, compTypes);
		assertEquals(comp, jpaEdmProv.getComplexType(new FullQualifiedName("salesorderprocessing", "Address")));
		try
		{
			jpaEdmProv.getComplexType(new FullQualifiedName("salesorderessing", "abc"));
		}
		catch(ODataJPAModelException e)
		{
			assertTrue(true);
		}
	}
	@Test
	public void testGetEntityContainerInfoWithBuffer() throws Exception
	{
		HashMap<String, EntityContainerInfo> entityContainerInfos = new HashMap<String, EntityContainerInfo>();
		EntityContainerInfo entityContainer = new EntityContainerInfo();
		entityContainer.setName("salesorderprocessingContainer");
		entityContainerInfos.put("salesorderprocessingContainer", entityContainer);
		ODataJPAEdmProvider jpaEdmProv = new ODataJPAEdmProvider();
		Class<?> claz = jpaEdmProv.getClass();
		Field f = claz.getDeclaredField("entityContainerInfos");
		f.setAccessible(true);
		f.set(jpaEdmProv, entityContainerInfos);
		assertEquals(entityContainer, jpaEdmProv.getEntityContainerInfo("salesorderprocessingContainer"));
		try
		{
			jpaEdmProv.getEntityContainerInfo("abc");
		}
		catch(ODataJPAModelException e)
		{
			assertTrue(true);
		}
	}
	@Test
	public void testGetEntityTypeWithBuffer() throws Exception
	{
		HashMap<String, com.sap.core.odata.api.edm.provider.EntityType> entityTypes = new HashMap<String,com.sap.core.odata.api.edm.provider.EntityType >();
		com.sap.core.odata.api.edm.provider.EntityType entity = new com.sap.core.odata.api.edm.provider.EntityType();
		entity.setName("SalesOrderHeader");
		entityTypes.put("salesorderprocessing"+"."+"SalesorderHeader", entity);
		ODataJPAEdmProvider jpaEdmProv = new ODataJPAEdmProvider();
		Class<?> claz = jpaEdmProv.getClass();
		Field f = claz.getDeclaredField("entityTypes");
		f.setAccessible(true);
		f.set(jpaEdmProv, entityTypes);
		assertEquals(entity, jpaEdmProv.getEntityType(new FullQualifiedName("salesorderprocessing", "SalesorderHeader")));
		try
		{
			jpaEdmProv.getEntityType(new FullQualifiedName("salesoprocessing", "abc"));
		}
		catch(ODataJPAModelException e)
		{
			assertTrue(true);
		}
	}
	@Test
	public void testGetAssociationWithBuffer() throws Exception
	{
		HashMap<String, Association> associations = new HashMap<String,Association >();
		Association association = new Association();
		association.setName("SalesOrderHeader_SalesOrderItem");
		associations.put("salesorderprocessing"+"."+"SalesOrderHeader_SalesOrderItem", association);
		ODataJPAEdmProvider jpaEdmProv = new ODataJPAEdmProvider();
		Class<?> claz = jpaEdmProv.getClass();
		Field f = claz.getDeclaredField("associations");
		f.setAccessible(true);
		f.set(jpaEdmProv, associations);
		assertEquals(association, jpaEdmProv.getAssociation(new FullQualifiedName("salesorderprocessing", "SalesOrderHeader_SalesOrderItem")));
		try
		{
			jpaEdmProv.getAssociation(new FullQualifiedName("salesorderprocessing", "abc"));
		}
		catch(ODataJPAModelException e)
		{
			assertTrue(true);
		}
	}


}
