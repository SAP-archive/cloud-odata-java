package com.sap.core.odata.processor.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.processor.jpa.exception.ODataJPAException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPAResultParserTest {

	private static String[] fields = { "field1", "field2" };

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
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		class demoItem {
			private String id;
			private int value;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
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
			EasyMock.expect(edmType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmType.getName()).andStubReturn("identifier");
			EasyMock.replay(edmType);
			EasyMock.expect(edmMapping.getInternalName()).andStubReturn("id");
			EasyMock.replay(edmMapping);
			EasyMock.expect(edmTyped.getType()).andStubReturn(edmType);
			EasyMock.expect(edmTyped.getMapping()).andStubReturn(edmMapping);
			EasyMock.replay(edmTyped);
			EasyMock.expect(structuralType.getProperty("identifier")).andStubReturn(
					edmTyped);
			

			
			EasyMock.expect(edmType01.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmType01.getName()).andStubReturn("value");
			EasyMock.replay(edmType01);
			EasyMock.expect(edmMapping01.getInternalName()).andStubReturn("value");
			EasyMock.replay(edmMapping01);
			EasyMock.expect(edmTyped01.getType()).andStubReturn(edmType01);
			EasyMock.expect(edmTyped01.getMapping()).andStubReturn(edmMapping01);
			EasyMock.replay(edmTyped01);
			EasyMock.expect(structuralType.getProperty("value")).andStubReturn(
					edmTyped01);
			
			
			List<String> propNames = new ArrayList<String>();
			propNames.add("identifier");
			propNames.add("value");
			EasyMock.expect(structuralType.getPropertyNames()).andReturn(propNames);
			EasyMock.replay(structuralType);

		} catch (EdmException e) {
			e.printStackTrace();
		}

		try {
			returnMap = resultParser.parse2EdmPropertyValueMap(jpaEntity,
					structuralType);
		} catch (ODataJPARuntimeException e) {
			fail("JUnit failed with exception "
					+ ODataJPARuntimeException.RUNTIME_EXCEPTION);
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
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);

		try {

			/*
			 * Case 1 - Property having No mapping
			 */
			EasyMock.expect(edmProperty.getName()).andStubReturn(fields[0]);
			EasyMock.expect(edmProperty.getMapping()).andStubReturn(null);
			EasyMock.replay(edmProperty);

			Method getGetterName = resultParser.getClass().getDeclaredMethod(
					"getGetterName", EdmProperty.class);
			getGetterName.setAccessible(true);

			String name = (String) getGetterName.invoke(resultParser,
					edmProperty);

			assertEquals("getField1", name);

		} catch (EdmException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (IllegalAccessException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (IllegalArgumentException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (InvocationTargetException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (NoSuchMethodException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (SecurityException e) {
			fail("Exception " + e.getMessage() + " not expected");

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
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);

		try {

			Method getGetterName = resultParser.getClass().getDeclaredMethod(
					"getGetterName", EdmProperty.class);
			getGetterName.setAccessible(true);

			/*
			 * Case 2 - Property having mapping
			 */
			EasyMock.expect(edmMapping.getInternalName()).andStubReturn(
					fields[1]);
			EasyMock.replay(edmMapping);

			edmProperty = EasyMock.createMock(EdmProperty.class);
			EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
			EasyMock.expect(edmProperty.getName()).andStubReturn(fields[0]);
			EasyMock.replay(edmProperty);

			String name = (String) getGetterName.invoke(resultParser,
					edmProperty);
			assertEquals("getField2", name);

		} catch (EdmException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (IllegalAccessException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (IllegalArgumentException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (InvocationTargetException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (NoSuchMethodException e) {
			fail("Exception " + e.getMessage() + " not expected");
		} catch (SecurityException e) {
			fail("Exception " + e.getMessage() + " not expected");

		}
	}
}
