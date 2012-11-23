package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.core.edm.Bit;
import com.sap.core.odata.core.edm.EdmBinary;
import com.sap.core.odata.core.edm.EdmBoolean;
import com.sap.core.odata.core.edm.EdmDateTime;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.edm.EdmDecimal;
import com.sap.core.odata.core.edm.EdmDouble;
import com.sap.core.odata.core.edm.EdmGuid;
import com.sap.core.odata.core.edm.EdmInt16;
import com.sap.core.odata.core.edm.EdmInt32;
import com.sap.core.odata.core.edm.EdmInt64;
import com.sap.core.odata.core.edm.EdmNull;
import com.sap.core.odata.core.edm.EdmSByte;
import com.sap.core.odata.core.edm.EdmSingle;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.core.edm.EdmTime;
import com.sap.core.odata.core.edm.Uint7;

/**
 * @author SAP AG
 */
public class EdmSimpleTypeTest {

  @Test
  public void testNameSpace() throws Exception {
    assertEquals(EdmSimpleTypeFacade.systemNamespace, Bit.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeFacade.systemNamespace, Uint7.getInstance().getNamespace());

    assertEquals(EdmSimpleTypeFacade.edmNamespace, EdmNull.getInstance().getNamespace());
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

  private void testCompatibility(final EdmSimpleType type, final EdmSimpleType... edmSimpleTypes) {
    for (EdmSimpleType compatible : edmSimpleTypes)
      assertTrue(type.isCompatible(compatible));
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
  public void toUriLiteralBinary() {
    assertEquals("binary'FA12AAA1'", EdmSimpleTypeFacade.binaryInstance().toUriLiteral("+hKqoQ=="));
  }

  @Test
  public void toUriLiteralBoolean() {
    assertEquals("true", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("true"));
    assertEquals("false", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("false"));
    assertEquals("0", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("0"));
    assertEquals("1", EdmSimpleTypeFacade.booleanInstance().toUriLiteral("1"));
  }

  @Test
  public void toUriLiteralByte() {
    assertEquals("127", EdmSimpleTypeFacade.byteInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralDateTime() {
    assertEquals("datetime'2009-12-26T21:23:38'", EdmSimpleTypeFacade.dateTimeInstance().toUriLiteral("2009-12-26T21:23:38"));
    assertEquals("datetime'2009-12-26T21:23:38Z'", EdmSimpleTypeFacade.dateTimeInstance().toUriLiteral("2009-12-26T21:23:38Z"));
  }

  @Test
  public void toUriLiteralDateTimeOffset() {
    assertEquals("datetimeoffset'2009-12-26T21:23:38Z'", EdmSimpleTypeFacade.dateTimeOffsetInstance().toUriLiteral("2009-12-26T21:23:38Z"));
    assertEquals("datetimeoffset'2002-10-10T12:00:00-05:00'", EdmSimpleTypeFacade.dateTimeOffsetInstance().toUriLiteral("2002-10-10T12:00:00-05:00"));
  }

  @Test
  public void toUriLiteralInt16() {
    assertEquals("127", EdmSimpleTypeFacade.int16Instance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralInt32() {
    assertEquals("127", EdmSimpleTypeFacade.int32Instance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralInt64() {
    assertEquals("127l", EdmSimpleTypeFacade.int64Instance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralSByte() {
    assertEquals("127", EdmSimpleTypeFacade.sByteInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralSingle() {
    assertEquals("127f", EdmSimpleTypeFacade.singleInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralString() {
    assertEquals("'StringValue'", EdmSimpleTypeFacade.stringInstance().toUriLiteral("StringValue"));
  }

  @Test
  public void toUriLiteralTime() {
    assertEquals("time'P120D'", EdmSimpleTypeFacade.timeInstance().toUriLiteral("P120D"));
  }

  private EdmFacets getMaxLengthFacets(final Integer maxLength) {
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.getMaxLength()).thenReturn(maxLength);
    return facets;
  }

  private EdmFacets getNullableFacets(final Boolean nullable) {
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.isNullable()).thenReturn(nullable);
    return facets;
  }

  private EdmFacets getDefaultFacets(final String defaultValue) {
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.getDefaultValue()).thenReturn(defaultValue);
    return facets;
  }

  private EdmFacets getPrecisionFacets(final Integer precision) {
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.getPrecision()).thenReturn(precision);
    return facets;
  }

  private void expectErrorInValueToString(final EdmSimpleType instance, final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      instance.valueToString(value, literalKind, facets);
      fail("Expected exception not thrown");
    } catch (RuntimeException e) {
      assertNotNull(e);
    }
  }

  private void checkNull(final EdmSimpleType instance) {
    assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, null));
    assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, getNullableFacets(true)));
    assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, getNullableFacets(null)));

    expectErrorInValueToString(instance, null, EdmLiteralKind.DEFAULT, getNullableFacets(false));

    assertEquals("default", instance.valueToString(null, EdmLiteralKind.DEFAULT, getDefaultFacets("default")));
  }

  @Test
  public void valueToStringBinary() {
    final byte[] binary = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF };
    final EdmSimpleType instance = EdmSimpleTypeFacade.binaryInstance();

    assertEquals("qrvM3e7/", instance.valueToString(binary, EdmLiteralKind.DEFAULT, null));
    assertEquals("qrvM3e7/", instance.valueToString(binary, EdmLiteralKind.JSON, null));
    assertEquals("binary'AABBCCDDEEFF'", instance.valueToString(binary, EdmLiteralKind.URI, null));

    assertEquals("qrvM3e7/", instance.valueToString(binary, EdmLiteralKind.DEFAULT, getMaxLengthFacets(6)));
    assertEquals("qrvM3e7/", instance.valueToString(binary, EdmLiteralKind.JSON, getMaxLengthFacets(6)));
    assertEquals("binary'AABBCCDDEEFF'", instance.valueToString(binary, EdmLiteralKind.URI, getMaxLengthFacets(6)));
    assertEquals("qrvM3e7/", instance.valueToString(binary, EdmLiteralKind.DEFAULT, getMaxLengthFacets(null)));

    assertEquals("qg==", instance.valueToString(new Byte[] {new Byte((byte) 170)}, EdmLiteralKind.DEFAULT, null));

    checkNull(instance);

    expectErrorInValueToString(instance, binary, EdmLiteralKind.DEFAULT, getMaxLengthFacets(3));
    expectErrorInValueToString(instance, binary, EdmLiteralKind.JSON, getMaxLengthFacets(3));
    expectErrorInValueToString(instance, binary, EdmLiteralKind.URI, getMaxLengthFacets(3));

    expectErrorInValueToString(instance, 0, EdmLiteralKind.DEFAULT, null);

    expectErrorInValueToString(instance, binary, null, null);
  }

  @Test
  public void valueToStringBoolean() {
    final EdmSimpleType instance = EdmSimpleTypeFacade.booleanInstance();

    assertEquals("true", instance.valueToString(true, EdmLiteralKind.DEFAULT, null));
    assertEquals("true", instance.valueToString(true, EdmLiteralKind.JSON, null));
    assertEquals("true", instance.valueToString(true, EdmLiteralKind.URI, null));
    assertEquals("false", instance.valueToString(Boolean.FALSE, EdmLiteralKind.DEFAULT, null));

    checkNull(instance);

    expectErrorInValueToString(instance, 0, EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueToStringByte() {
    final EdmSimpleType instance = EdmSimpleTypeFacade.byteInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString((Integer) 32, EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));

    checkNull(instance);

    expectErrorInValueToString(instance, -1, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 256, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 'A', EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueToStringDateTime() {
    final EdmSimpleType instance = EdmSimpleTypeFacade.dateTimeInstance();
    Calendar dateTime = Calendar.getInstance();

    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT+11:30"));
    dateTime.set(2012, 2, 1, 11, 2, 3);
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330558323000)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetime'2012-02-29T23:32:03'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    dateTime.add(Calendar.MILLISECOND, 1);
    assertEquals("2012-02-29T23:32:03.001", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330558323001)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetime'2012-02-29T23:32:03.001'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    final Long millis = 1330558323007L;
    assertEquals("2012-02-29T23:32:03.007", instance.valueToString(millis, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(" + millis + ")\\/", instance.valueToString(millis, EdmLiteralKind.JSON, null));
    assertEquals("datetime'2012-02-29T23:32:03.007'", instance.valueToString(millis, EdmLiteralKind.URI, null));

    assertEquals("2012-02-29T23:32:03.007", instance.valueToString(new Date(millis), EdmLiteralKind.DEFAULT, null));

    assertEquals("2012-02-29T23:32:03.00", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionFacets(2)));
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionFacets(0)));
    dateTime.add(Calendar.MILLISECOND, -14);
    assertEquals("2012-02-29T23:32:02.987", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionFacets(null)));
    assertEquals("2012-02-29T23:32:02.987", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionFacets(5)));
    assertEquals("2012-02-29T23:32:02.99", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionFacets(2)));
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionFacets(1)));
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionFacets(0)));

    checkNull(instance);

    expectErrorInValueToString(instance, 0, EdmLiteralKind.DEFAULT, null);
  }
}