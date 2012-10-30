package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.core.edm.simpletype.EdmBit;
import com.sap.core.odata.core.edm.simpletype.EdmUint7;

public class EdmSimpleTypeTest {

  private static final EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();

  private void testCompatibility(final EdmSimpleType type, final EdmSimpleType... edmSimpleTypes) {
    for (EdmSimpleType compatible : edmSimpleTypes)
      assertTrue(type.isCompatible(compatible));
  }

  @Test
  public void testNames() throws Exception {
    assertEquals("Binary", facade.binaryInstance().getName());
    assertEquals("Boolean", facade.booleanInstance().getName());
    assertEquals("Byte", facade.byteInstance().getName());
    assertEquals("DateTime", facade.dateTimeInstance().getName());
    assertEquals("DateTimeOffset", facade.dateTimeOffsetInstance().getName());
    assertEquals("Decimal", facade.decimalInstance().getName());
    assertEquals("Double", facade.doubleInstance().getName());
    assertEquals("Guid", facade.guidInstance().getName());
    assertEquals("Int16", facade.int16Instance().getName());
    assertEquals("Int32", facade.int32Instance().getName());
    assertEquals("Int64", facade.int64Instance().getName());
    assertEquals("SByte", facade.sByteInstance().getName());
    assertEquals("Single", facade.singleInstance().getName());
    assertEquals("String", facade.stringInstance().getName());
    assertEquals("Time", facade.timeInstance().getName());
  }

  @Test
  public void testBinaryCompatibility() {
    testCompatibility(facade.binaryInstance(),
        facade.binaryInstance());
  }

  @Test
  public void testBooleanCompatibility() {
    testCompatibility(facade.booleanInstance(),
        facade.booleanInstance(),
        new EdmBit());
  }

  @Test
  public void testByteCompatibility() {
    testCompatibility(facade.byteInstance(),
        facade.byteInstance(),
        new EdmBit(),
        new EdmUint7());
  }

  @Test
  public void testDateTimeCompatibility() {
    testCompatibility(facade.dateTimeInstance(),
        facade.dateTimeInstance());
  }

  @Test
  public void testDateTimeOffsetCompatibility() {
    testCompatibility(facade.dateTimeOffsetInstance(),
        facade.dateTimeOffsetInstance());
  }

  @Test
  public void testDecimalCompatibility() {
    testCompatibility(facade.decimalInstance(),
        new EdmBit(),
        new EdmUint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance(),
        facade.int32Instance(),
        facade.int64Instance(),
        facade.singleInstance(),
        facade.doubleInstance(),
        facade.decimalInstance());
  }

  @Test
  public void testDoubleCompatibility() {
    testCompatibility(facade.doubleInstance(),
        new EdmBit(),
        new EdmUint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance(),
        facade.int32Instance(),
        facade.int64Instance(),
        facade.singleInstance(),
        facade.doubleInstance());
  }

  @Test
  public void testGuidCompatibility() {
    testCompatibility(facade.guidInstance(),
        facade.guidInstance());
  }

  @Test
  public void testint16Compatibility() {
    testCompatibility(facade.int16Instance(),
        new EdmBit(),
        new EdmUint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance());
  }

  @Test
  public void testInt32Compatibility() {
    testCompatibility(facade.int32Instance(),
        new EdmBit(),
        new EdmUint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance(),
        facade.int32Instance());
  }

  @Test
  public void testInt64Compatibility() {
    testCompatibility(facade.int64Instance(),
        new EdmBit(),
        new EdmUint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance(),
        facade.int32Instance(),
        facade.int64Instance());
  }

  @Test
  public void testSByteCompatibility() {
    testCompatibility(facade.sByteInstance(),
        new EdmBit(),
        new EdmUint7(),
        facade.sByteInstance());
  }

  @Test
  public void testSingleCompatibility() {
    testCompatibility(facade.singleInstance(),
        new EdmBit(),
        new EdmUint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance(),
        facade.int32Instance(),
        facade.int64Instance(),
        facade.singleInstance());
  }

  @Test
  public void testStringCompatibility() {
    testCompatibility(facade.stringInstance(),
        facade.stringInstance());
  }

  @Test
  public void testTimeCompatibility() {
    testCompatibility(facade.timeInstance(),
        facade.timeInstance());
  }
}
