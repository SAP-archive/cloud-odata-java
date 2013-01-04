package com.sap.core.odata.processor.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;

public class JPAResultParserTest {

	private static String[] fields = { "field1", "field2" };
	
	/*
	 * TestCase - JPAResultParser is a singleton class
	 * Check if the same instance is returned when
	 * create method is called
	 */
	@Test
	public void testCreate() {
		JPAResultParser resultParser1 = JPAResultParser.create();
		JPAResultParser resultParser2 = JPAResultParser.create();

		assertEquals(resultParser1, resultParser2);
	}

	@Test
	public void testParse2EdmEntity() {
		fail("Not yet implemented");
	}
	
	/*
	 * TestCase - getGetterName is a private method in
	 * JPAResultParser. The method is uses reflection to
	 * derive the property access methods from EdmProperty
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
			
			Method getGetterName = resultParser.getClass()
					.getDeclaredMethod("getGetterName", EdmProperty.class);
			getGetterName.setAccessible(true);
			
			String name = (String) getGetterName.invoke(resultParser, edmProperty);

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
	 * TestCase - getGetterName is a private method in
	 * JPAResultParser. The method is uses reflection to
	 * derive the property access methods from EdmProperty
	 * 
	 * EdmProperty name could have been modified. Then mapping
	 * object of EdmProperty should be used for deriving the
	 * name
	 */
	@Test
	public void testGetGettersWithMapping() {
		JPAResultParser resultParser = JPAResultParser.create();
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		
		try {

		
			Method getGetterName = resultParser.getClass()
					.getDeclaredMethod("getGetterName", EdmProperty.class);
			getGetterName.setAccessible(true);
			
			/*
			 * Case 2 - Property having mapping
			 */
			EasyMock.expect(edmMapping.getInternalName()).andStubReturn(fields[1]);
			EasyMock.replay(edmMapping);
			
			edmProperty = EasyMock.createMock(EdmProperty.class);
			EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
			EasyMock.expect(edmProperty.getName()).andStubReturn(fields[0]);
			EasyMock.replay(edmProperty);
			
		
			String name = (String) getGetterName.invoke(resultParser, edmProperty);
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
