package com.sap.core.odata.processor.core.jpa.access.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

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
    UUID uUID = new UUID(0, 0);

    try {
      edmSimpleKindTypeString = JPATypeConvertor
          .convertToEdmSimpleType(str.getClass(), null);
      edmSimpleKindTypeByteArr = JPATypeConvertor
          .convertToEdmSimpleType(byteArr.getClass(), null);
      edmSimpleKindTypeLong = JPATypeConvertor
          .convertToEdmSimpleType(longObj.getClass(), null);
      edmSimpleKindTypeShort = JPATypeConvertor
          .convertToEdmSimpleType(shortObj.getClass(), null);
      edmSimpleKindTypeInteger = JPATypeConvertor
          .convertToEdmSimpleType(integerObj.getClass(), null);
      edmSimpleKindTypeDouble = JPATypeConvertor
          .convertToEdmSimpleType(doubleObj.getClass(), null);
      edmSimpleKindTypeFloat = JPATypeConvertor
          .convertToEdmSimpleType(floatObj.getClass(), null);
      edmSimpleKindTypeBigDecimal = JPATypeConvertor
          .convertToEdmSimpleType(bigDecimalObj.getClass(), null);
      edmSimpleKindTypeByte = JPATypeConvertor
          .convertToEdmSimpleType(byteObj.getClass(), null);
      edmSimpleKindTypeBoolean = JPATypeConvertor
          .convertToEdmSimpleType(booleanObj.getClass(), null);
      /*edmSimpleKindTypeDate = JPATypeConvertor
      		.convertToEdmSimpleType(dateObj.getClass(),null);*/
      edmSimpleKindTypeUUID = JPATypeConvertor
          .convertToEdmSimpleType(uUID.getClass(), null);
    } catch (ODataJPAModelException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
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
    //assertEquals(EdmSimpleTypeKind.DateTime, edmSimpleKindTypeDate);
    assertEquals(EdmSimpleTypeKind.Guid, edmSimpleKindTypeUUID);
  }

}
