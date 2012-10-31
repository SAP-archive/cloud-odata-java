package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.core.edm.simpletype.Bit;
import com.sap.core.odata.core.edm.simpletype.Uint7;

public class EdmSimpleTypeTest {

  private static final EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();

  private void testCompatibility(final EdmSimpleType type, final EdmSimpleType... edmSimpleTypes) {
    for (EdmSimpleType compatible : edmSimpleTypes)
      assertTrue(type.isCompatible(compatible));
  }
  
  
  @Test
  public void toUriLiteralBinary(){
    assertEquals("binary'FA12AAA1'", facade.binaryInstance().toUriLiteral("+hKqoQ=="));
  }
  
  @Test
  public void toUriLiteralBoolean(){
    assertEquals("true", facade.booleanInstance().toUriLiteral("true"));
    assertEquals("false", facade.booleanInstance().toUriLiteral("false"));
    assertEquals("0", facade.booleanInstance().toUriLiteral("0"));
    assertEquals("1", facade.booleanInstance().toUriLiteral("1"));
  }

  @Test
  public void toUriLiteralByte(){
    assertEquals("127", facade.byteInstance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralDateTime(){
    assertEquals("datetime'2009-12-26T21%3A23%3A38'", facade.dateTimeInstance().toUriLiteral("2009-12-26T21:23:38"));
    assertEquals("datetime'2009-12-26T21%3A23%3A38Z'", facade.dateTimeInstance().toUriLiteral("2009-12-26T21:23:38Z"));
  }
  
  @Test
  public void toUriLiteralDateTimeOffset(){
    assertEquals("datetimeoffset'2009-12-26T21%3A23%3A38Z'", facade.dateTimeOffsetInstance().toUriLiteral("2009-12-26T21:23:38Z"));
    assertEquals("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'", facade.dateTimeOffsetInstance().toUriLiteral("2002-10-10T12:00:00-05:00"));
  }
  
  @Test
  public void toUriLiteralInt16(){
    assertEquals("127", facade.int16Instance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralInt32(){
    assertEquals("127", facade.int32Instance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralInt64(){
    assertEquals("127l", facade.int64Instance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralSByte(){
    assertEquals("127", facade.sByteInstance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralSingle(){
    assertEquals("127f", facade.singleInstance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralString(){
    assertEquals("'StringValue'", facade.stringInstance().toUriLiteral("StringValue"));
  }
  
  @Test
  public void toUriLiteralTime(){
    assertEquals("time'P120D'", facade.timeInstance().toUriLiteral("P120D"));
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
        new Bit());
  }

  @Test
  public void testByteCompatibility() {
    testCompatibility(facade.byteInstance(),
        facade.byteInstance(),
        new Bit(),
        new Uint7());
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
        new Bit(),
        new Uint7(),
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
        new Bit(),
        new Uint7(),
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
        new Bit(),
        new Uint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance());
  }

  @Test
  public void testInt32Compatibility() {
    testCompatibility(facade.int32Instance(),
        new Bit(),
        new Uint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance(),
        facade.int32Instance());
  }

  @Test
  public void testInt64Compatibility() {
    testCompatibility(facade.int64Instance(),
        new Bit(),
        new Uint7(),
        facade.byteInstance(),
        facade.sByteInstance(),
        facade.int16Instance(),
        facade.int32Instance(),
        facade.int64Instance());
  }

  @Test
  public void testSByteCompatibility() {
    testCompatibility(facade.sByteInstance(),
        new Bit(),
        new Uint7(),
        facade.sByteInstance());
  }

  @Test
  public void testSingleCompatibility() {
    testCompatibility(facade.singleInstance(),
        new Bit(),
        new Uint7(),
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
