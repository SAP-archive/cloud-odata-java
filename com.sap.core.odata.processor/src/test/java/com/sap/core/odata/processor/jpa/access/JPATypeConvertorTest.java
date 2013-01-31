package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.processor.jpa.access.model.JPATypeConvertor;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;

public class JPATypeConvertorTest {

	private EdmSimpleTypeKind edmSimpleKindTypeString;
	private EdmSimpleTypeKind edmSimpleKindTypeByteArr;
	private EdmSimpleTypeKind edmSimpleKindTypeLong;
	private EdmSimpleTypeKind edmSimpleKindTypeShort;
	private EdmSimpleTypeKind edmSimpleKindTypeInteger;
	private EdmSimpleTypeKind edmSimpleKindTypeDouble;
	private EdmSimpleTypeKind edmSimpleKindTypeFloat;
	private EdmSimpleTypeKind edmSimpleKindTypeBigDecimal;
	private EdmSimpleTypeKind edmSimpleKindTypeByte;
	private EdmSimpleTypeKind edmSimpleKindTypeBoolean;
	private EdmSimpleTypeKind edmSimpleKindTypeDate;
	//private EdmSimpleTypeKind edmSimpleKindTypeCalendar;
	private EdmSimpleTypeKind edmSimpleKindTypeUUID;
	
	@Test
	public void testConvertToEdmSimpleType() {
		String str = "entity";
		byte[] byteArr = new byte[3];
		Long longObj = new Long(0);
		Short shortObj = new Short((short) 0);
		Integer integerObj = new Integer(0);
		Double doubleObj = new Double(0);
		Float floatObj = new Float(0);
		BigDecimal bigDecimalObj = new BigDecimal(0);
		Byte byteObj = new Byte((byte) 0);
		Boolean booleanObj = Boolean.TRUE;
		Date dateObj = Calendar.getInstance().getTime();
		//Calendar cal = Calendar.getInstance();
		UUID uUID = new UUID(0, 0);
		
		try {
			edmSimpleKindTypeString = JPATypeConvertor.convertToEdmSimpleType(str.getClass());
			edmSimpleKindTypeByteArr = JPATypeConvertor.convertToEdmSimpleType(byteArr.getClass());
			edmSimpleKindTypeLong = JPATypeConvertor.convertToEdmSimpleType(longObj.getClass());
			edmSimpleKindTypeShort = JPATypeConvertor.convertToEdmSimpleType(shortObj.getClass());
			edmSimpleKindTypeInteger = JPATypeConvertor.convertToEdmSimpleType(integerObj.getClass());
			edmSimpleKindTypeDouble = JPATypeConvertor.convertToEdmSimpleType(doubleObj.getClass());
			edmSimpleKindTypeFloat = JPATypeConvertor.convertToEdmSimpleType(floatObj.getClass());
			edmSimpleKindTypeBigDecimal = JPATypeConvertor.convertToEdmSimpleType(bigDecimalObj.getClass());
			edmSimpleKindTypeByte = JPATypeConvertor.convertToEdmSimpleType(byteObj.getClass());
			edmSimpleKindTypeBoolean = JPATypeConvertor.convertToEdmSimpleType(booleanObj.getClass());
			edmSimpleKindTypeDate = JPATypeConvertor.convertToEdmSimpleType(dateObj.getClass());
			//edmSimpleKindTypeCalendar = JPATypeConvertor.convertToEdmSimpleType(cal.getClass());
			edmSimpleKindTypeUUID = JPATypeConvertor.convertToEdmSimpleType(uUID.getClass());
		} catch (ODataJPAModelException e) {
			fail("ODATA Model Exception raised");
		}
		assertEquals(EdmSimpleTypeKind.String, edmSimpleKindTypeString);
		assertEquals(EdmSimpleTypeKind.Binary, edmSimpleKindTypeByteArr);
		assertEquals(EdmSimpleTypeKind.Int64, edmSimpleKindTypeLong);
		assertEquals(EdmSimpleTypeKind.Int16, edmSimpleKindTypeShort);
		assertEquals(EdmSimpleTypeKind.Int32, edmSimpleKindTypeInteger);
		assertEquals(EdmSimpleTypeKind.Double, edmSimpleKindTypeDouble);
		assertEquals(EdmSimpleTypeKind.Single, edmSimpleKindTypeFloat);
		assertEquals(EdmSimpleTypeKind.Decimal, edmSimpleKindTypeBigDecimal);
		assertEquals(EdmSimpleTypeKind.Byte, edmSimpleKindTypeByte);
		assertEquals(EdmSimpleTypeKind.Boolean, edmSimpleKindTypeBoolean);
		assertEquals(EdmSimpleTypeKind.DateTime, edmSimpleKindTypeDate);
		//assertEquals(EdmSimpleTypeKind.DateTime, edmSimpleKindTypeCalendar);
		assertEquals(EdmSimpleTypeKind.Guid, edmSimpleKindTypeUUID);
	}

}
