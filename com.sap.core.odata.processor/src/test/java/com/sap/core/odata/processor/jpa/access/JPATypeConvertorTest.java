package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPATypeConvertorTest {

	private EdmSimpleTypeKind edmSimpleKindTypeString;
	private EdmSimpleTypeKind edmSimpleKindTypeBinary;
	@Test
	public void testConvertToEdmSimpleType() {
		Byte[] byteArr = new Byte[3];
		String str = "entity";
		try {
			edmSimpleKindTypeString = JPATypeConvertor.convertToEdmSimpleType(str.getClass());
			edmSimpleKindTypeBinary = JPATypeConvertor.convertToEdmSimpleType(byteArr.getClass());
		} catch (ODataJPAModelException e) {
			fail("ODATA Model Exception raised");
		}
		assertEquals(EdmSimpleTypeKind.String, edmSimpleKindTypeString);
		assertEquals(EdmSimpleTypeKind.Binary, edmSimpleKindTypeBinary);
	}

}
