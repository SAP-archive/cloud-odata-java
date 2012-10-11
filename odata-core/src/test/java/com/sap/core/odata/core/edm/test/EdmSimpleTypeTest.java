package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.odata4j.core.Guid;
import org.odata4j.core.UnsignedByte;
import com.sap.core.odata.core.edm.EdmSimpleType;

public class EdmSimpleTypeTest {

  @Test
  public void testNames() {

    assertEquals("Edm.Binary", EdmSimpleType.BINARY.getName());
    assertEquals("Edm.Boolean", EdmSimpleType.BOOLEAN.getName());
    assertEquals("Edm.Byte", EdmSimpleType.BYTE.getName());
    assertEquals("Edm.DateTime", EdmSimpleType.DATETIME.getName());
    assertEquals("Edm.DateTimeOffset", EdmSimpleType.DATETIMEOFFSET.getName());
    assertEquals("Edm.Decimal", EdmSimpleType.DECIMAL.getName());
    assertEquals("Edm.Double", EdmSimpleType.DOUBLE.getName());
    assertEquals("Edm.Guid", EdmSimpleType.GUID.getName());
    assertEquals("Edm.Int16", EdmSimpleType.INT16.getName());
    assertEquals("Edm.Int32", EdmSimpleType.INT32.getName());
    assertEquals("Edm.Int64", EdmSimpleType.INT64.getName());
    assertEquals("Edm.SByte", EdmSimpleType.SBYTE.getName());
    assertEquals("Edm.Single", EdmSimpleType.SINGLE.getName());
    assertEquals("Edm.String", EdmSimpleType.STRING.getName());
    assertEquals("Edm.Time", EdmSimpleType.TIME.getName());
  }

  @Test
  public void testCannonicalType() {

    assertEquals(byte[].class, EdmSimpleType.BINARY.getCanonicalJavaType());
    assertEquals(Boolean.class, EdmSimpleType.BOOLEAN.getCanonicalJavaType());
    assertEquals(UnsignedByte.class, EdmSimpleType.BYTE.getCanonicalJavaType());
    assertEquals(LocalDateTime.class, EdmSimpleType.DATETIME.getCanonicalJavaType());
    assertEquals(DateTime.class, EdmSimpleType.DATETIMEOFFSET.getCanonicalJavaType());
    assertEquals(BigDecimal.class, EdmSimpleType.DECIMAL.getCanonicalJavaType());
    assertEquals(Double.class, EdmSimpleType.DOUBLE.getCanonicalJavaType());
    assertEquals(Guid.class, EdmSimpleType.GUID.getCanonicalJavaType());
    assertEquals(Short.class, EdmSimpleType.INT16.getCanonicalJavaType());
    assertEquals(Integer.class, EdmSimpleType.INT32.getCanonicalJavaType());
    assertEquals(Long.class, EdmSimpleType.INT64.getCanonicalJavaType());
    assertEquals(Byte.class, EdmSimpleType.SBYTE.getCanonicalJavaType());
    assertEquals(Float.class, EdmSimpleType.SINGLE.getCanonicalJavaType());
    assertEquals(String.class, EdmSimpleType.STRING.getCanonicalJavaType());
    assertEquals(LocalTime.class, EdmSimpleType.TIME.getCanonicalJavaType());
  }

  @Test
  public void testAlternativeTypes() {

    assertEquals(Byte[].class, EdmSimpleType.BINARY.getAlternativeJavaTypes()[0]);
    // Incomplete
  }

}
