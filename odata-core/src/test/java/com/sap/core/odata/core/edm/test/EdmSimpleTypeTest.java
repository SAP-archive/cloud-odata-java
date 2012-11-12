package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.core.edm.simpletype.Bit;
import com.sap.core.odata.core.edm.simpletype.EdmBinary;
import com.sap.core.odata.core.edm.simpletype.EdmBoolean;
import com.sap.core.odata.core.edm.simpletype.EdmDateTime;
import com.sap.core.odata.core.edm.simpletype.EdmDateTimeOffset;
import com.sap.core.odata.core.edm.simpletype.EdmDecimal;
import com.sap.core.odata.core.edm.simpletype.EdmDouble;
import com.sap.core.odata.core.edm.simpletype.EdmGuid;
import com.sap.core.odata.core.edm.simpletype.EdmInt16;
import com.sap.core.odata.core.edm.simpletype.EdmInt32;
import com.sap.core.odata.core.edm.simpletype.EdmInt64;
import com.sap.core.odata.core.edm.simpletype.EdmNull;
import com.sap.core.odata.core.edm.simpletype.EdmSByte;
import com.sap.core.odata.core.edm.simpletype.EdmSingle;
import com.sap.core.odata.core.edm.simpletype.EdmString;
import com.sap.core.odata.core.edm.simpletype.EdmTime;
import com.sap.core.odata.core.edm.simpletype.Uint7;

public class EdmSimpleTypeTest {

  private void testCompatibility(final EdmSimpleType type, final EdmSimpleType... edmSimpleTypes) {
    for (EdmSimpleType compatible : edmSimpleTypes)
      assertTrue(type.isCompatible(compatible));
  }
  
  
  @Test
  public void toUriLiteralBinary(){
    assertEquals("binary'FA12AAA1'", EdmSimpleTypeFacade.binaryInstance().toUriLiteral("+hKqoQ=="));
  }
  
  @Test
  public void toUriLiteralBoolean(){
    assertEquals("true", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("true"));
    assertEquals("false", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("false"));
    assertEquals("0", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("0"));
    assertEquals("1", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("1"));
  }

  @Test
  public void toUriLiteralByte(){
    assertEquals("127", EdmSimpleTypeFacade.byteInstance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralDateTime(){
    assertEquals("datetime'2009-12-26T21%3A23%3A38'", EdmSimpleTypeFacade.dateTimeInstance().toUriLiteral("2009-12-26T21:23:38"));
    assertEquals("datetime'2009-12-26T21%3A23%3A38Z'", EdmSimpleTypeFacade.dateTimeInstance().toUriLiteral("2009-12-26T21:23:38Z"));
  }
  
  @Test
  public void toUriLiteralDateTimeOffset(){
    assertEquals("datetimeoffset'2009-12-26T21%3A23%3A38Z'", EdmSimpleTypeFacade.dateTimeOffsetInstance().toUriLiteral("2009-12-26T21:23:38Z"));
    assertEquals("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'", EdmSimpleTypeFacade.dateTimeOffsetInstance().toUriLiteral("2002-10-10T12:00:00-05:00"));
  }
  
  @Test
  public void toUriLiteralInt16(){
    assertEquals("127", EdmSimpleTypeFacade.int16Instance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralInt32(){
    assertEquals("127", EdmSimpleTypeFacade.int32Instance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralInt64(){
    assertEquals("127l", EdmSimpleTypeFacade.int64Instance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralSByte(){
    assertEquals("127", EdmSimpleTypeFacade.sByteInstance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralSingle(){
    assertEquals("127f", EdmSimpleTypeFacade.singleInstance().toUriLiteral("127"));
  }
  
  @Test
  public void toUriLiteralString(){
    assertEquals("'StringValue'", EdmSimpleTypeFacade.stringInstance().toUriLiteral("StringValue"));
  }
  
  @Test
  public void toUriLiteralTime(){
    assertEquals("time'P120D'", EdmSimpleTypeFacade.timeInstance().toUriLiteral("P120D"));
  }
  
  @Test
  public void testNames() throws Exception {
    assertEquals("Binary", EdmSimpleTypeFacade.binaryInstance().getName());
    assertEquals("Boolean", EdmSimpleTypeFacade.booleanInstance().getName());
    assertEquals("Byte", EdmSimpleTypeFacade.byteInstance().getName());
    assertEquals("DateTime", EdmSimpleTypeFacade.dateTimeInstance().getName());
    assertEquals("DateTimeOffset", EdmSimpleTypeFacade.dateTimeOffsetInstance().getName());
    assertEquals("Decimal", EdmSimpleTypeFacade.decimalInstance().getName());
    assertEquals("Double", EdmSimpleTypeFacade.doubleInstance().getName());
    assertEquals("Guid", EdmSimpleTypeFacade.guidInstance().getName());
    assertEquals("Int16", EdmSimpleTypeFacade.int16Instance().getName());
    assertEquals("Int32", EdmSimpleTypeFacade.int32Instance().getName());
    assertEquals("Int64", EdmSimpleTypeFacade.int64Instance().getName());
    assertEquals("SByte", EdmSimpleTypeFacade.sByteInstance().getName());
    assertEquals("Single", EdmSimpleTypeFacade.singleInstance().getName());
    assertEquals("String", EdmSimpleTypeFacade.stringInstance().getName());
    assertEquals("Time", EdmSimpleTypeFacade.timeInstance().getName());
  }

  @Test
  public void testBinaryCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.binaryInstance(),
        EdmSimpleTypeFacade.binaryInstance());
  }

  @Test
  public void testBooleanCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.booleanInstance(),
        EdmSimpleTypeFacade.booleanInstance(),
        Bit.getInstance());
  }

  @Test
  public void testByteCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.byteInstance(),
        EdmSimpleTypeFacade.byteInstance(),
        Bit.getInstance(),
        Uint7.getInstance());
  }

  @Test
  public void testDateTimeCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.dateTimeInstance(),
        EdmSimpleTypeFacade.dateTimeInstance());
  }

  @Test
  public void testDateTimeOffsetCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.dateTimeOffsetInstance(),
        EdmSimpleTypeFacade.dateTimeOffsetInstance());
  }

  @Test
  public void testDecimalCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.decimalInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeFacade.byteInstance(),
        EdmSimpleTypeFacade.sByteInstance(),
        EdmSimpleTypeFacade.int16Instance(),
        EdmSimpleTypeFacade.int32Instance(),
        EdmSimpleTypeFacade.int64Instance(),
        EdmSimpleTypeFacade.singleInstance(),
        EdmSimpleTypeFacade.doubleInstance(),
        EdmSimpleTypeFacade.decimalInstance());
  }

  @Test
  public void testDoubleCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.doubleInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeFacade.byteInstance(),
        EdmSimpleTypeFacade.sByteInstance(),
        EdmSimpleTypeFacade.int16Instance(),
        EdmSimpleTypeFacade.int32Instance(),
        EdmSimpleTypeFacade.int64Instance(),
        EdmSimpleTypeFacade.singleInstance(),
        EdmSimpleTypeFacade.doubleInstance());
  }

  @Test
  public void testGuidCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.guidInstance(),
        EdmSimpleTypeFacade.guidInstance());
  }

  @Test
  public void testint16Compatibility() {
    testCompatibility(EdmSimpleTypeFacade.int16Instance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeFacade.byteInstance(),
        EdmSimpleTypeFacade.sByteInstance(),
        EdmSimpleTypeFacade.int16Instance());
  }

  @Test
  public void testInt32Compatibility() {
    testCompatibility(EdmSimpleTypeFacade.int32Instance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeFacade.byteInstance(),
        EdmSimpleTypeFacade.sByteInstance(),
        EdmSimpleTypeFacade.int16Instance(),
        EdmSimpleTypeFacade.int32Instance());
  }

  @Test
  public void testInt64Compatibility() {
    testCompatibility(EdmSimpleTypeFacade.int64Instance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeFacade.byteInstance(),
        EdmSimpleTypeFacade.sByteInstance(),
        EdmSimpleTypeFacade.int16Instance(),
        EdmSimpleTypeFacade.int32Instance(),
        EdmSimpleTypeFacade.int64Instance());
  }

  @Test
  public void testSByteCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.sByteInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeFacade.sByteInstance());
  }

  @Test
  public void testSingleCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.singleInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeFacade.byteInstance(),
        EdmSimpleTypeFacade.sByteInstance(),
        EdmSimpleTypeFacade.int16Instance(),
        EdmSimpleTypeFacade.int32Instance(),
        EdmSimpleTypeFacade.int64Instance(),
        EdmSimpleTypeFacade.singleInstance());
  }

  @Test
  public void testStringCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.stringInstance(),
        EdmSimpleTypeFacade.stringInstance());
  }

  @Test
  public void testTimeCompatibility() {
    testCompatibility(EdmSimpleTypeFacade.timeInstance(),
        EdmSimpleTypeFacade.timeInstance());
  }
  
  @Test
  public void testNameSpace() throws Exception {
    assertEquals(EdmSimpleTypeFacade.systemNamespace, Bit.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.systemNamespace, Uint7.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.systemNamespace, EdmNull.getInstance().getNamespace());
    
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmBinary.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmBoolean.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmDateTime.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmDateTimeOffset.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmDecimal.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmDouble.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmGuid.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmInt16.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmInt32.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmInt64.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmSByte.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmSingle.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmString.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmTime.getInstance().getNamespace());

  }
  
}
