package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
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
    assertEquals(EdmSimpleTypeKind.systemNamespace, Bit.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.systemNamespace, Uint7.getInstance().getNamespace());

    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmNull.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmBinary.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmBoolean.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmDateTime.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmDateTimeOffset.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmDecimal.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmDouble.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmGuid.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmInt16.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmInt32.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmInt64.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmSByte.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmSingle.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmString.getInstance().getNamespace());
    assertEquals(EdmSimpleTypeKind.edmNamespace, EdmTime.getInstance().getNamespace());
  }

  @Test
  public void testNames() throws Exception {
    assertEquals("Binary", EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance().getName());
    assertEquals("Boolean", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance().getName());
    assertEquals("Byte", EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance().getName());
    assertEquals("DateTime", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance().getName());
    assertEquals("DateTimeOffset", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance().getName());
    assertEquals("Decimal", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance().getName());
    assertEquals("Double", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance().getName());
    assertEquals("Guid", EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance().getName());
    assertEquals("Int16", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance().getName());
    assertEquals("Int32", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance().getName());
    assertEquals("Int64", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance().getName());
    assertEquals("SByte", EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance().getName());
    assertEquals("Single", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance().getName());
    assertEquals("String", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance().getName());
    assertEquals("Time", EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance().getName());
  }

  private void testCompatibility(final EdmSimpleType type, final EdmSimpleType... edmSimpleTypes) {
    for (EdmSimpleType compatible : edmSimpleTypes)
      assertTrue(type.isCompatible(compatible));
  }

  @Test
  public void testBinaryCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance());
  }

  @Test
  public void testBooleanCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(),
        Bit.getInstance());
  }

  @Test
  public void testByteCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance());
  }

  @Test
  public void testDateTimeCompatibility() {
    testCompatibility(EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance());
  }

  @Test
  public void testDateTimeOffsetCompatibility() {
    testCompatibility(EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance());
  }

  @Test
  public void testDecimalCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance());
  }

  @Test
  public void testDoubleCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance());
  }

  @Test
  public void testGuidCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance());
  }

  @Test
  public void testint16Compatibility() {
    testCompatibility(EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance());
  }

  @Test
  public void testInt32Compatibility() {
    testCompatibility(EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance());
  }

  @Test
  public void testInt64Compatibility() {
    testCompatibility(EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance());
  }

  @Test
  public void testSByteCompatibility() {
    testCompatibility(EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance());
  }

  @Test
  public void testSingleCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance());
  }

  @Test
  public void testStringCompatibility() {
    testCompatibility(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
  }

  @Test
  public void testTimeCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance());
  }

  @Test
  public void toUriLiteralBinary() throws Exception {
    assertEquals("binary'FA12AAA1'", EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance().toUriLiteral("+hKqoQ=="));
  }

  @Test
  public void toUriLiteralBoolean() throws Exception {
    assertEquals("true", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance().toUriLiteral("true"));
    assertEquals("false", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance().toUriLiteral("false"));
    assertEquals("0", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance().toUriLiteral("0"));
    assertEquals("1", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance().toUriLiteral("1"));
  }

  @Test
  public void toUriLiteralByte() throws Exception {
    assertEquals("127", EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralDateTime() throws Exception {
    assertEquals("datetime'2009-12-26T21:23:38'", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance().toUriLiteral("2009-12-26T21:23:38"));
    assertEquals("datetime'2009-12-26T21:23:38Z'", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance().toUriLiteral("2009-12-26T21:23:38Z"));
  }

  @Test
  public void toUriLiteralDateTimeOffset() throws Exception {
    assertEquals("datetimeoffset'2009-12-26T21:23:38Z'", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance().toUriLiteral("2009-12-26T21:23:38Z"));
    assertEquals("datetimeoffset'2002-10-10T12:00:00-05:00'", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance().toUriLiteral("2002-10-10T12:00:00-05:00"));
  }

  @Test
  public void toUriLiteralInt16() throws Exception {
    assertEquals("127", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralInt32() throws Exception {
    assertEquals("127", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralInt64() throws Exception {
    assertEquals("127l", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralSByte() throws Exception {
    assertEquals("127", EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralSingle() throws Exception {
    assertEquals("127f", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralString() throws Exception {
    assertEquals("'StringValue'", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance().toUriLiteral("StringValue"));
  }

  @Test
  public void toUriLiteralTime() throws Exception {
    assertEquals("time'P120D'", EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance().toUriLiteral("P120D"));
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

  private EdmFacets getPrecisionScaleFacets(final Integer precision, final Integer scale) {
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.getPrecision()).thenReturn(precision);
    when(facets.getScale()).thenReturn(scale);
    return facets;
  }

  private void expectErrorInValueToString(final EdmSimpleType instance, final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      instance.valueToString(value, literalKind, facets);
      fail("Expected exception not thrown");
    } catch (EdmSimpleTypeException e) {
      assertNotNull(e);
    }
  }

  private void checkNull(final EdmSimpleType instance) throws EdmSimpleTypeException {
    assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, null));
    assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, getNullableFacets(true)));
    assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, getNullableFacets(null)));

    expectErrorInValueToString(instance, null, EdmLiteralKind.DEFAULT, getNullableFacets(false));

    assertEquals("default", instance.valueToString(null, EdmLiteralKind.DEFAULT, getDefaultFacets("default")));
  }

  @Test
  public void valueToStringBinary() throws Exception {
    final byte[] binary = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF };
    final EdmSimpleType instance = EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance();

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
  public void valueToStringBoolean() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance();

    assertEquals("true", instance.valueToString(true, EdmLiteralKind.DEFAULT, null));
    assertEquals("true", instance.valueToString(true, EdmLiteralKind.JSON, null));
    assertEquals("true", instance.valueToString(true, EdmLiteralKind.URI, null));
    assertEquals("false", instance.valueToString(Boolean.FALSE, EdmLiteralKind.DEFAULT, null));

    checkNull(instance);

    expectErrorInValueToString(instance, 0, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, false, null, null);
  }

  @Test
  public void valueToStringByte() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance();

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
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringDateTime() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance();
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

    assertEquals("2012-02-29T23:32:03.00", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null)));
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null)));
    dateTime.add(Calendar.MILLISECOND, -14);
    assertEquals("2012-02-29T23:32:02.987", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, null)));
    assertEquals("2012-02-29T23:32:02.987", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null)));
    assertEquals("2012-02-29T23:32:02.99", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null)));
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null)));
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null)));

    checkNull(instance);

    expectErrorInValueToString(instance, 0, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, dateTime, null, null);
  }

  @Test
  public void valueToStringDateTimeOffset() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance();
    Calendar dateTime = Calendar.getInstance();

    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
    dateTime.set(2012, 1, 29, 1, 2, 3);
    assertEquals("2012-02-29T01:02:03Z", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330477323000)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetimeoffset'2012-02-29T01:02:03Z'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    dateTime.setTimeZone(TimeZone.getTimeZone("GMT-1:30"));
    assertEquals("2012-02-29T01:02:03-01:30", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330477323000-0130)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetimeoffset'2012-02-29T01:02:03-01:30'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    dateTime.setTimeZone(TimeZone.getTimeZone("GMT+11:00"));
    assertEquals("2012-02-29T01:02:03+11:00", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330477323000+1100)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetimeoffset'2012-02-29T01:02:03+11:00'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    final Long millis = 1330558323007L;
    assertEquals("2012-02-29T23:32:03.007Z", instance.valueToString(millis, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(" + millis + ")\\/", instance.valueToString(millis, EdmLiteralKind.JSON, null));
    assertEquals("datetimeoffset'2012-02-29T23:32:03.007Z'", instance.valueToString(millis, EdmLiteralKind.URI, null));

    final Date date = new Date(millis);
    final String time = date.toString().substring(11, 19);
    assertTrue(instance.valueToString(date, EdmLiteralKind.DEFAULT, null).contains(time));

    checkNull(instance);

    expectErrorInValueToString(instance, 0, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, dateTime, null, null);
  }

  @Test
  public void valueToStringDecimal() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0M", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString((Integer) 32, EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));
    assertEquals("123456789012345678901234567890", instance.valueToString(new BigInteger("123456789012345678901234567890"), EdmLiteralKind.DEFAULT, null));
    assertEquals("0.00390625", instance.valueToString(1.0 / 256, EdmLiteralKind.DEFAULT, null));
    assertEquals("-0.125", instance.valueToString(-0.125f, EdmLiteralKind.DEFAULT, null));
    assertEquals("-1234567890.1234567890", instance.valueToString(new BigDecimal("-1234567890.1234567890"), EdmLiteralKind.DEFAULT, null));

    assertEquals("-32768", instance.valueToString(-32768, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, null)));
    assertEquals("0.5", instance.valueToString(0.5, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, null)));

    assertEquals("-32768", instance.valueToString(-32768, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(42, null)));
    assertEquals("-32768", instance.valueToString(-32768, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null)));
    assertEquals("32768", instance.valueToString(32768, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null)));
    assertEquals("0.5", instance.valueToString(0.5, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null)));
    assertEquals("0.5", instance.valueToString(0.5, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, 1)));
    expectErrorInValueToString(instance, -1234, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null));
    expectErrorInValueToString(instance, 1234, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(3, null));
    expectErrorInValueToString(instance, 0.00390625, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null));
    expectErrorInValueToString(instance, 0.00390625, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, 7));

    checkNull(instance);

    expectErrorInValueToString(instance, 'A', EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }
}