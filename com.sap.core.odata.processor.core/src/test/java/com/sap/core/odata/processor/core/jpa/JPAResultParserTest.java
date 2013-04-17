package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.EasyMock;
import org.junit.Test;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

public class JPAResultParserTest {
	/*
	 * TestCase - JPAResultParser is a singleton class Check if the same
	 * instance is returned when create method is called
	 */
	@Test
	public void testCreate() {
		JPAResultParser resultParser1 = JPAResultParser.create();
		JPAResultParser resultParser2 = JPAResultParser.create();

		assertEquals(resultParser1, resultParser2);
	}

	@Test
	public void testparse2EdmPropertyValueMap() {
		JPAResultParser resultParser = JPAResultParser.create();
		Object jpaEntity = new demoItem("abc", 10);
		EdmStructuralType structuralType = EasyMock
				.createMock(EdmStructuralType.class);
		EdmProperty edmTyped = EasyMock.createMock(EdmProperty.class);
		EdmType edmType = EasyMock.createMock(EdmType.class);
		EdmProperty edmTyped01 = EasyMock.createMock(EdmProperty.class);
		EdmType edmType01 = EasyMock.createMock(EdmType.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EdmMapping edmMapping01 = EasyMock.createMock(EdmMapping.class);

		try {
			EasyMock.expect(edmType.getKind())
					.andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmTyped.getName()).andStubReturn("identifier");
			EasyMock.replay(edmType);
			EasyMock.expect(edmMapping.getInternalName()).andStubReturn("id");
			EasyMock.replay(edmMapping);
			EasyMock.expect(edmTyped.getType()).andStubReturn(edmType);
			EasyMock.expect(edmTyped.getMapping()).andStubReturn(edmMapping);
			EasyMock.replay(edmTyped);
			EasyMock.expect(structuralType.getProperty("identifier"))
					.andStubReturn(edmTyped);

			EasyMock.expect(edmType01.getKind()).andStubReturn(
					EdmTypeKind.SIMPLE);
			EasyMock.expect(edmTyped01.getName()).andStubReturn("Value");
			EasyMock.replay(edmType01);
			EasyMock.expect(edmMapping01.getInternalName()).andStubReturn(
					"value");
			EasyMock.replay(edmMapping01);
			EasyMock.expect(edmTyped01.getType()).andStubReturn(edmType01);
			EasyMock.expect(edmTyped01.getMapping())
					.andStubReturn(edmMapping01);
			EasyMock.replay(edmTyped01);
			EasyMock.expect(structuralType.getProperty("value")).andStubReturn(
					edmTyped01);

			List<String> propNames = new ArrayList<String>();
			propNames.add("identifier");
			propNames.add("value");
			EasyMock.expect(structuralType.getPropertyNames()).andReturn(
					propNames);
			EasyMock.replay(structuralType);

		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}

		try {
			Map<String,Object> result = resultParser.parse2EdmPropertyValueMap(jpaEntity, structuralType);
			assertEquals(2, result.size());
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}

	}

	@Test
	public void testparse2EdmPropertyValueMapEdmExcep() {
		JPAResultParser resultParser = JPAResultParser.create();
		Object jpaEntity = new demoItem("abc", 10);
		EdmStructuralType structuralType = EasyMock
				.createMock(EdmStructuralType.class);
		EdmProperty edmTyped = EasyMock.createMock(EdmProperty.class);
		EdmType edmType = EasyMock.createMock(EdmType.class);
		EdmProperty edmTyped01 = EasyMock.createMock(EdmProperty.class);
		EdmType edmType01 = EasyMock.createMock(EdmType.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EdmMapping edmMapping01 = EasyMock.createMock(EdmMapping.class);

		try {
			EasyMock.expect(edmType.getKind())
					.andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmType.getName()).andStubThrow(
					new EdmException(null));
			EasyMock.replay(edmType);
			EasyMock.expect(edmMapping.getInternalName()).andStubReturn("id");
			EasyMock.replay(edmMapping);
			EasyMock.expect(edmTyped.getType()).andStubThrow(
					new EdmException(null));
			EasyMock.expect(edmTyped.getMapping()).andStubReturn(edmMapping);
			EasyMock.replay(edmTyped);
			EasyMock.expect(structuralType.getProperty("identifier"))
					.andStubReturn(edmTyped);

			EasyMock.expect(edmType01.getKind()).andStubReturn(
					EdmTypeKind.SIMPLE);
			EasyMock.expect(edmType01.getName()).andStubReturn("value");
			EasyMock.replay(edmType01);
			EasyMock.expect(edmMapping01.getInternalName()).andStubReturn(
					"value");
			EasyMock.replay(edmMapping01);
			EasyMock.expect(edmTyped01.getType()).andStubReturn(edmType01);
			EasyMock.expect(edmTyped01.getMapping())
					.andStubReturn(edmMapping01);
			EasyMock.replay(edmTyped01);
			EasyMock.expect(structuralType.getProperty("value")).andStubReturn(
					edmTyped01);

			List<String> propNames = new ArrayList<String>();
			propNames.add("identifier");
			propNames.add("value");
			EasyMock.expect(structuralType.getPropertyNames()).andReturn(
					propNames);
			EasyMock.replay(structuralType);

		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2); // assertTrue(false);
		}

		try {
			resultParser.parse2EdmPropertyValueMap(jpaEntity, structuralType);
		} catch (ODataJPARuntimeException e) {
			assertTrue(true);
		}

	}
	
	@Test
	public void testparse2EdmPropertyListMap()
	{
		JPAResultParser resultParser = JPAResultParser.create();
		Map<String,Object> edmEntity = new HashMap<String, Object>();
		edmEntity.put("SoId", 1);
		DemoRelatedEntity relatedEntity = new DemoRelatedEntity("NewOrder");
		demoItem jpaEntity = new demoItem("laptop",1);
		jpaEntity.setRelatedEntity(relatedEntity);
		List<EdmNavigationProperty> navigationPropertyList = new ArrayList<EdmNavigationProperty>();
		// Mocking a navigation property and its mapping object
		EdmNavigationProperty navigationProperty = EasyMock.createMock(EdmNavigationProperty.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		try {
			EasyMock.expect(edmMapping.getInternalName()).andStubReturn("relatedEntity");
			EasyMock.replay(edmMapping);
			EasyMock.expect(navigationProperty.getName()).andStubReturn("RelatedEntities");
			EasyMock.expect(navigationProperty.getMapping()).andStubReturn(edmMapping);
			EasyMock.replay(navigationProperty);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
		navigationPropertyList.add(navigationProperty);
		try {
			resultParser.parse2EdmPropertyListMap(edmEntity, jpaEntity, navigationPropertyList);
			assertEquals(relatedEntity, edmEntity.get("RelatedEntities"));
			
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}
	@Test
	public void testparse2EdmPropertyValueMapFromList()
	{
		JPAResultParser resultParser = JPAResultParser.create();
		demoItem jpaEntity = new demoItem("laptop", 1);
		DemoRelatedEntity relatedEntity = new DemoRelatedEntity("DemoOrder");
		jpaEntity.setRelatedEntity(relatedEntity);
		List<EdmProperty> selectPropertyList = new ArrayList<EdmProperty>();
		// Mocking EdmProperties
		EdmProperty edmProperty1 = EasyMock.createMock(EdmProperty.class);
		EdmProperty edmProperty2 = EasyMock.createMock(EdmProperty.class);
		EdmType edmType1 = EasyMock.createMock(EdmType.class);
		EdmType edmType2 = EasyMock.createMock(EdmType.class);
		EdmMapping mapping1 = EasyMock.createMock(EdmMapping.class);
		EdmMapping mapping2 = EasyMock.createMock(EdmMapping.class);
		try {
			EasyMock.expect(edmType1.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.replay(edmType1);
			EasyMock.expect(mapping1.getInternalName()).andStubReturn("id");
			EasyMock.replay(mapping1);
			EasyMock.expect(edmProperty1.getName()).andStubReturn("Id");
			EasyMock.expect(edmProperty1.getMapping()).andStubReturn(mapping1);
			EasyMock.expect(edmProperty1.getType()).andStubReturn(edmType1);
			EasyMock.replay(edmProperty1);
			EasyMock.expect(edmType2.getKind()).andStubReturn(EdmTypeKind.COMPLEX);
			EasyMock.replay(edmType2);
			EasyMock.expect(mapping2.getInternalName()).andStubReturn("relatedEntity.order");
			EasyMock.replay(mapping2);
			EasyMock.expect(edmProperty2.getName()).andStubReturn("Order");
			EasyMock.expect(edmProperty2.getMapping()).andStubReturn(mapping2);
			EasyMock.expect(edmProperty2.getType()).andStubReturn(edmType2);
			EasyMock.replay(edmProperty2);
			
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		selectPropertyList.add(edmProperty1);
		selectPropertyList.add(edmProperty2);
		try {
			Map<String,Object> result = resultParser.parse2EdmPropertyValueMapFromList(jpaEntity, selectPropertyList);
			assertEquals("DemoOrder", result.get("Order"));
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
	}
	// This unit tests when there is a complex type in the select list
	@SuppressWarnings("unchecked")
	@Test
	public void testparse2EdmPropertyValueMapFromListComplex()
	{
		JPAResultParser resultParser = JPAResultParser.create();
		demoItem jpaEntity = new demoItem("laptop", 1);
		DemoRelatedEntity relatedEntity = new DemoRelatedEntity("DemoOrder");
		jpaEntity.setRelatedEntity(relatedEntity);
		List<EdmProperty> selectPropertyList = new ArrayList<EdmProperty>();
		// Mocking EdmProperties
		EdmProperty edmProperty1 = EasyMock.createMock(EdmProperty.class);
		EdmProperty edmProperty2 = EasyMock.createMock(EdmProperty.class);
		EdmProperty edmComplexProperty = EasyMock.createMock(EdmProperty.class);
		EdmType edmType1 = EasyMock.createMock(EdmType.class);
		EdmStructuralType edmType2 = EasyMock.createMock(EdmStructuralType.class);
		EdmType edmComplexType = EasyMock.createMock(EdmType.class);
		EdmMapping mapping1 = EasyMock.createMock(EdmMapping.class);
		EdmMapping mapping2 = EasyMock.createMock(EdmMapping.class);
		EdmMapping complexMapping = EasyMock.createMock(EdmMapping.class);
		try {
			EasyMock.expect(edmType1.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.replay(edmType1);
			EasyMock.expect(mapping1.getInternalName()).andStubReturn("id");
			EasyMock.replay(mapping1);
			EasyMock.expect(edmProperty1.getName()).andStubReturn("Id");
			EasyMock.expect(edmProperty1.getMapping()).andStubReturn(mapping1);
			EasyMock.expect(edmProperty1.getType()).andStubReturn(edmType1);
			EasyMock.replay(edmProperty1);
			// Mocking the complex properties
			EasyMock.expect(edmComplexType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.replay(edmComplexType);
			EasyMock.expect(complexMapping.getInternalName()).andStubReturn("order");
			EasyMock.replay(complexMapping);
			EasyMock.expect(edmComplexProperty.getName()).andStubReturn("OrderName");
			EasyMock.expect(edmComplexProperty.getMapping()).andStubReturn(complexMapping);
			EasyMock.expect(edmComplexProperty.getType()).andStubReturn(edmComplexType);
			EasyMock.replay(edmComplexProperty);
			EasyMock.expect(edmType2.getKind()).andStubReturn(EdmTypeKind.COMPLEX);
			EasyMock.expect(edmType2.getProperty("OrderName")).andStubReturn(edmComplexProperty);
			List<String> propertyNames = new ArrayList<String>();
			propertyNames.add("OrderName");
			EasyMock.expect(edmType2.getPropertyNames()).andStubReturn(propertyNames);
			EasyMock.replay(edmType2);
			EasyMock.expect(mapping2.getInternalName()).andStubReturn("relatedEntity");
			EasyMock.replay(mapping2);
			EasyMock.expect(edmProperty2.getName()).andStubReturn("Order");
			EasyMock.expect(edmProperty2.getMapping()).andStubReturn(mapping2);
			EasyMock.expect(edmProperty2.getType()).andStubReturn(edmType2);
			EasyMock.replay(edmProperty2);
			
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		selectPropertyList.add(edmProperty1);
		selectPropertyList.add(edmProperty2);
		try {
			Map<String,Object> result = resultParser.parse2EdmPropertyValueMapFromList(jpaEntity, selectPropertyList);
			assertEquals(1, ((HashMap<String, Object>)result.get("Order")).size());
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
	}


	/*
	 * TestCase - getGetterName is a private method in JPAResultParser. The
	 * method is uses reflection to derive the property access methods from
	 * EdmProperty
	 */
	@Test
	public void testGetGettersWithOutMapping() {
		JPAResultParser resultParser = JPAResultParser.create();
		try {

			/*
			 * Case 1 - Property having No mapping
			 */
			Class<?>[] pars = {String.class,EdmMapping.class};
			Object[] params = {"Field1",null};
			Method getGetterName = resultParser.getClass().getDeclaredMethod(
					"getGetterName", pars);
			getGetterName.setAccessible(true);

			String name = (String) getGetterName.invoke(resultParser,
					params);

			assertEquals("getField1", name);

		} catch (IllegalAccessException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (IllegalArgumentException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (InvocationTargetException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (NoSuchMethodException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (SecurityException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);

		}
	}

	@Test
	public void testGetGettersWithNullPropname() {
		JPAResultParser resultParser = JPAResultParser.create();
		try {

			/*
			 * Case 1 - Property having No mapping and no name
			 */
			Class<?>[] pars = {String.class,EdmMapping.class};
			Object[] params = {null,null};
			Method getGetterName = resultParser.getClass().getDeclaredMethod(
					"getGetterName", pars);
			getGetterName.setAccessible(true);

			String name = (String) getGetterName.invoke(resultParser,
					params);
			assertNull(name);

		} catch (IllegalAccessException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (IllegalArgumentException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (InvocationTargetException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (NoSuchMethodException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (SecurityException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);

		}
	}

	/*
	 * TestCase - getGetterName is a private method in JPAResultParser. The
	 * method is uses reflection to derive the property access methods from
	 * EdmProperty
	 * 
	 * EdmProperty name could have been modified. Then mapping object of
	 * EdmProperty should be used for deriving the name
	 */
	@Test
	public void testGetGettersWithMapping() {
		JPAResultParser resultParser = JPAResultParser.create();
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn("field1");
		EasyMock.replay(edmMapping);
		try {

			Class<?>[] pars = {String.class,EdmMapping.class};
			Object[] params = {"myField",edmMapping};
			Method getGetterName = resultParser.getClass().getDeclaredMethod(
					"getGetterName", pars);
			getGetterName.setAccessible(true);

			String name = (String) getGetterName.invoke(resultParser,
					params);
			assertEquals("getField1", name);

		} catch (IllegalAccessException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (IllegalArgumentException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (InvocationTargetException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (NoSuchMethodException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (SecurityException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);

		}
	}

	@Test
	public void testGetGettersNoSuchMethodException() {
		JPAResultParser resultParser = JPAResultParser.create();
		try {

			Method getGetterName = resultParser.getClass().getDeclaredMethod(
					"getGetterName1", EdmProperty.class);
			getGetterName.setAccessible(true);

		} catch (NoSuchMethodException e) {
			assertEquals(
					"com.sap.core.odata.processor.core.jpa.JPAResultParser.getGetterName1(com.sap.core.odata.api.edm.EdmProperty)",
					e.getMessage());
		} catch (SecurityException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);

		}
	}
	
	@Test
	public void testParse2EdmPropertyValueMap() {
		JPAResultParser resultParser = JPAResultParser.create();
		Object jpaEntity = new DemoItem2("abc");
		try {
			resultParser.parse2EdmPropertyValueMapFromList(jpaEntity, getEdmPropertyList()); 
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	@Test
	public void testGetGetterEdmException() {
		JPAResultParser resultParser = JPAResultParser.create();
		Object jpaEntity = new demoItem("abc", 10);
		EdmStructuralType structuralType = EasyMock
				.createMock(EdmStructuralType.class);
		try {
			EasyMock.expect(structuralType.getPropertyNames()).andStubThrow(
					new EdmException(null));
			EasyMock.replay(structuralType);
			Method getGetters = resultParser.getClass().getDeclaredMethod(
					"getGetters", Object.class, EdmStructuralType.class);
			getGetters.setAccessible(true);
			try {
				getGetters.invoke(resultParser, jpaEntity, structuralType);
			} catch (IllegalAccessException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (IllegalArgumentException e) {
				fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
						+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
			} catch (InvocationTargetException e) {
				assertTrue(true);
			}
		} catch (NoSuchMethodException e) {
			assertEquals(
					"com.sap.core.odata.processor.jpa.JPAResultParser.getGetterName1(com.sap.core.odata.api.edm.EdmProperty)",
					e.getMessage());
		} catch (SecurityException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	@Test
	public void testForNullJPAEntity() {
		JPAResultParser resultParser = JPAResultParser.create();
		EdmStructuralType structuralType = EasyMock
				.createMock(EdmStructuralType.class);
		Object map;
		try {
			map = resultParser.parse2EdmPropertyValueMap(null,
					structuralType);
			assertNull(map);
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	class demoItem {
		private String id;
		private int value;
		private DemoRelatedEntity relatedEntity;
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public DemoRelatedEntity getRelatedEntity() {
			return relatedEntity;
		}

		public void setRelatedEntity(DemoRelatedEntity relatedEntity) {
			this.relatedEntity = relatedEntity;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		demoItem(String id, int value) {
			this.id = id;
			this.value = value;
		}

	}
	class DemoRelatedEntity
	{
		String order;

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

		public DemoRelatedEntity(String order) {
			super();
			this.order = order;
		}
		
	}
	
	private List<EdmProperty> getEdmPropertyList() {
		List<EdmProperty> properties = new ArrayList<EdmProperty>();
		properties.add(getEdmProperty());
		return properties;
	}
	
	class DemoItem2
	{
		private String field1;

		public String getField1() {
			return field1;
		}

		public void setField1(String field) {
			this.field1 = field;
		}
		
		public DemoItem2(String field)
		{
			this.field1 = field;
		}
		
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
}
