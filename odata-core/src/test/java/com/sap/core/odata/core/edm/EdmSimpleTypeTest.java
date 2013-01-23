package com.sap.core.odata.core.edm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class EdmSimpleTypeTest extends BaseTest {

  @Test
  public void testNameSpace() throws Exception {
    assertEquals(EdmSimpleType.SYSTEM_NAMESPACE, Bit.getInstance().getNamespace());
    assertEquals(EdmSimpleType.SYSTEM_NAMESPACE, Uint7.getInstance().getNamespace());

    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmNull.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmBinary.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmBoolean.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmDateTime.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmDateTimeOffset.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmDecimal.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmDouble.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmGuid.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmInt16.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmInt32.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmInt64.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmSByte.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmSingle.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmString.getInstance().getNamespace());
    assertEquals(EdmSimpleType.EDM_NAMESPACE, EdmTime.getInstance().getNamespace());
  }

  @Test
  public void testNames() throws Exception {
    assertEquals("Bit", Bit.getInstance().getName());
    assertEquals("Uint7", Uint7.getInstance().getName());
    assertEquals("Null", EdmNull.getInstance().getName());

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

  @Test
  public void testKind() throws Exception {
    for (EdmSimpleTypeKind kind : EdmSimpleTypeKind.values())
      assertEquals(EdmTypeKind.SIMPLE, kind.getEdmSimpleTypeInstance().getKind());
  }

  private void testCompatibility(final EdmSimpleType type, final EdmSimpleType... edmSimpleTypes) {
    for (EdmSimpleType compatible : edmSimpleTypes)
      assertTrue(type.isCompatible(compatible));
  }

  @Test
  public void testUint7Compatibility() {
    testCompatibility(Uint7.getInstance(), Uint7.getInstance(), Bit.getInstance());
    assertFalse(Uint7.getInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testBinaryCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testBooleanCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(),
        Bit.getInstance());
    assertFalse(EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testByteCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance());
    assertFalse(EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testDateTimeCompatibility() {
    testCompatibility(EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testDateTimeOffsetCompatibility() {
    testCompatibility(EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
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
    assertFalse(EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
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
    assertFalse(EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testGuidCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testInt16Compatibility() {
    testCompatibility(EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
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
    assertFalse(EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
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
    assertFalse(EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testSByteCompatibility() {
    testCompatibility(EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(),
        Bit.getInstance(),
        Uint7.getInstance(),
        EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
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
    assertFalse(EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testStringCompatibility() {
    testCompatibility(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()));
  }

  @Test
  public void testTimeCompatibility() {
    testCompatibility(EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance(),
        EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance());
    assertFalse(EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance().isCompatible(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()));
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
    assertEquals("datetime'2009-12-26T21%3A23%3A38'", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance().toUriLiteral("2009-12-26T21:23:38"));
    assertEquals("datetime'2009-12-26T21%3A23%3A38Z'", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance().toUriLiteral("2009-12-26T21:23:38Z"));
  }

  @Test
  public void toUriLiteralDateTimeOffset() throws Exception {
    assertEquals("datetimeoffset'2009-12-26T21%3A23%3A38Z'", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance().toUriLiteral("2009-12-26T21:23:38Z"));
    assertEquals("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance().toUriLiteral("2002-10-10T12:00:00-05:00"));
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
    assertEquals("127L", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralSByte() throws Exception {
    assertEquals("127", EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance().toUriLiteral("127"));
  }

  @Test
  public void toUriLiteralSingle() throws Exception {
    assertEquals("127F", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance().toUriLiteral("127"));
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

  private EdmFacets getUnicodeFacets(final Boolean unicode) {
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.isUnicode()).thenReturn(unicode);
    when(facets.getMaxLength()).thenReturn(null);
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

  @Test
  public void checkNull() throws Exception {
    for (EdmSimpleTypeKind kind : EdmSimpleTypeKind.values()) {
      if (kind == EdmSimpleTypeKind.Null)
        continue;
      final EdmSimpleType instance = kind.getEdmSimpleTypeInstance();
      assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, null));
      assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, getNullableFacets(true)));
      assertNull(instance.valueToString(null, EdmLiteralKind.DEFAULT, getNullableFacets(null)));

      expectErrorInValueToString(instance, null, EdmLiteralKind.DEFAULT, getNullableFacets(false));

      assertEquals("default", instance.valueToString(null, EdmLiteralKind.DEFAULT, getDefaultFacets("default")));
    }
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

    assertEquals("qg==", instance.valueToString(new Byte[] { new Byte((byte) 170) }, EdmLiteralKind.DEFAULT, null));

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
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));

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
    assertEquals("datetime'2012-02-29T23%3A32%3A03'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    dateTime.add(Calendar.MILLISECOND, 1);
    assertEquals("2012-02-29T23:32:03.001", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330558323001)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetime'2012-02-29T23%3A32%3A03.001'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    final Long millis = 1330558323007L;
    assertEquals("2012-02-29T23:32:03.007", instance.valueToString(millis, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(" + millis + ")\\/", instance.valueToString(millis, EdmLiteralKind.JSON, null));
    assertEquals("datetime'2012-02-29T23%3A32%3A03.007'", instance.valueToString(millis, EdmLiteralKind.URI, null));

    assertEquals("2012-02-29T23:32:03.007", instance.valueToString(new Date(millis), EdmLiteralKind.DEFAULT, null));

    dateTime.add(Calendar.MILLISECOND, 9);
    assertEquals("2012-02-29T23:32:03.01", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null)));
    dateTime.add(Calendar.MILLISECOND, -10);
    assertEquals("2012-02-29T23:32:03", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null)));
    dateTime.add(Calendar.MILLISECOND, -13);
    assertEquals("2012-02-29T23:32:02.987", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, null)));
    assertEquals("2012-02-29T23:32:02.98700", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null)));
    dateTime.add(Calendar.MILLISECOND, 3);
    assertEquals("2012-02-29T23:32:02.99", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null)));
    dateTime.add(Calendar.MILLISECOND, -90);
    assertEquals("2012-02-29T23:32:02.9", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null)));

    expectErrorInValueToString(instance, dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null));

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
    assertEquals("datetimeoffset'2012-02-29T01%3A02%3A03Z'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    dateTime.setTimeZone(TimeZone.getTimeZone("GMT-1:30"));
    assertEquals("2012-02-29T01:02:03-01:30", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330477323000-0090)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetimeoffset'2012-02-29T01%3A02%3A03-01%3A30'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    dateTime.setTimeZone(TimeZone.getTimeZone("GMT+11:00"));
    assertEquals("2012-02-29T01:02:03+11:00", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(1330477323000+0660)\\/", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("datetimeoffset'2012-02-29T01%3A02%3A03+11%3A00'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    final Long millis = 1330558323007L;
    assertEquals("2012-02-29T23:32:03.007Z", instance.valueToString(millis, EdmLiteralKind.DEFAULT, null));
    assertEquals("\\/Date(" + millis + ")\\/", instance.valueToString(millis, EdmLiteralKind.JSON, null));
    assertEquals("datetimeoffset'2012-02-29T23%3A32%3A03.007Z'", instance.valueToString(millis, EdmLiteralKind.URI, null));

    final Date date = new Date(millis);
    final String time = date.toString().substring(11, 19);
    assertTrue(instance.valueToString(date, EdmLiteralKind.DEFAULT, null).contains(time));

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
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
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
    assertEquals("100", instance.valueToString(new BigDecimal(BigInteger.ONE, -2), EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(3, null)));

    expectErrorInValueToString(instance, -1234, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null));
    expectErrorInValueToString(instance, 1234, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(3, null));
    expectErrorInValueToString(instance, 0.00390625, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null));
    expectErrorInValueToString(instance, 0.00390625, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, 7));

    expectErrorInValueToString(instance, 'A', EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, Double.NaN, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringDouble() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0D", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));
    assertEquals("0.00390625", instance.valueToString(1.0 / 256, EdmLiteralKind.DEFAULT, null));
    assertEquals("4.2E-41", instance.valueToString(42e-42, EdmLiteralKind.DEFAULT, null));
    assertEquals("INF", instance.valueToString(Double.POSITIVE_INFINITY, EdmLiteralKind.DEFAULT, null));
    assertEquals("-0.125", instance.valueToString(-0.125f, EdmLiteralKind.DEFAULT, null));
    assertEquals("-INF", instance.valueToString(Float.NEGATIVE_INFINITY, EdmLiteralKind.DEFAULT, null));
    assertEquals("-1234567890.12345", instance.valueToString(new BigDecimal("-1234567890.12345"), EdmLiteralKind.DEFAULT, null));

    expectErrorInValueToString(instance, 1234567890123456L, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, new BigDecimal("1234567890123456"), EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, new BigDecimal(BigInteger.TEN, 400), EdmLiteralKind.DEFAULT, null);

    expectErrorInValueToString(instance, 'A', EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringGuid() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance();
    final UUID uuid = UUID.randomUUID();

    assertEquals(uuid.toString(), instance.valueToString(uuid, EdmLiteralKind.DEFAULT, null));
    assertEquals(uuid.toString(), instance.valueToString(uuid, EdmLiteralKind.JSON, null));
    assertEquals("guid'" + uuid.toString() + "'", instance.valueToString(uuid, EdmLiteralKind.URI, null));

    expectErrorInValueToString(instance, 'A', EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringInt16() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));

    expectErrorInValueToString(instance, 123456, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, -32769, EdmLiteralKind.DEFAULT, null);

    expectErrorInValueToString(instance, 1.0, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringInt32() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));

    expectErrorInValueToString(instance, 12345678901L, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, -2147483649L, EdmLiteralKind.DEFAULT, null);

    expectErrorInValueToString(instance, 1.0, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringInt64() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0L", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));
    assertEquals("12345678901L", instance.valueToString(12345678901L, EdmLiteralKind.URI, null));
    assertEquals("1234567890123456789", instance.valueToString(new BigInteger("1234567890123456789"), EdmLiteralKind.DEFAULT, null));
    assertEquals("-1234567890123456789L", instance.valueToString(new BigInteger("-1234567890123456789"), EdmLiteralKind.URI, null));

    expectErrorInValueToString(instance, new BigInteger("123456789012345678901"), EdmLiteralKind.DEFAULT, null);

    expectErrorInValueToString(instance, 1.0, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringSByte() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
    assertEquals("64", instance.valueToString(64L, EdmLiteralKind.DEFAULT, null));

    expectErrorInValueToString(instance, -129, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 128, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 'A', EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringSingle() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance();

    assertEquals("0", instance.valueToString(0, EdmLiteralKind.DEFAULT, null));
    assertEquals("0", instance.valueToString(0, EdmLiteralKind.JSON, null));
    assertEquals("0F", instance.valueToString(0, EdmLiteralKind.URI, null));
    assertEquals("8", instance.valueToString((byte) 8, EdmLiteralKind.DEFAULT, null));
    assertEquals("16", instance.valueToString((short) 16, EdmLiteralKind.DEFAULT, null));
    assertEquals("32", instance.valueToString(Integer.valueOf(32), EdmLiteralKind.DEFAULT, null));
    assertEquals("255", instance.valueToString(255L, EdmLiteralKind.DEFAULT, null));
    assertEquals("0.00390625", instance.valueToString(1.0 / 256, EdmLiteralKind.DEFAULT, null));
    assertEquals("4.2E-8", instance.valueToString(42e-9, EdmLiteralKind.DEFAULT, null));
    assertEquals("INF", instance.valueToString(Double.POSITIVE_INFINITY, EdmLiteralKind.DEFAULT, null));
    assertEquals("-0.125", instance.valueToString(-0.125f, EdmLiteralKind.DEFAULT, null));
    assertEquals("-INF", instance.valueToString(Float.NEGATIVE_INFINITY, EdmLiteralKind.DEFAULT, null));
    assertEquals("-12345.67", instance.valueToString(new BigDecimal("-12345.67"), EdmLiteralKind.DEFAULT, null));

    expectErrorInValueToString(instance, 12345678L, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, new BigDecimal("12345678"), EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, new BigDecimal(BigInteger.TEN, 39), EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 42e38, EdmLiteralKind.DEFAULT, null);

    expectErrorInValueToString(instance, 'A', EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, 1, null, null);
  }

  @Test
  public void valueToStringString() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.String.getEdmSimpleTypeInstance();

    assertEquals("text", instance.valueToString("text", EdmLiteralKind.DEFAULT, null));
    assertEquals("a\nb", instance.valueToString("a\nb", EdmLiteralKind.JSON, null));
    assertEquals("'true'", instance.valueToString(true, EdmLiteralKind.URI, null));
    assertEquals("'a''b'", instance.valueToString("a'b", EdmLiteralKind.URI, null));

    assertEquals("text", instance.valueToString("text", EdmLiteralKind.DEFAULT, getUnicodeFacets(true)));
    assertEquals("text", instance.valueToString("text", EdmLiteralKind.DEFAULT, getUnicodeFacets(null)));
    assertEquals("text", instance.valueToString("text", EdmLiteralKind.DEFAULT, getMaxLengthFacets(4)));
    assertEquals("text", instance.valueToString("text", EdmLiteralKind.DEFAULT, getMaxLengthFacets(null)));

    expectErrorInValueToString(instance, "schräg", EdmLiteralKind.DEFAULT, getUnicodeFacets(false));
    expectErrorInValueToString(instance, "text", EdmLiteralKind.DEFAULT, getMaxLengthFacets(3));

    expectErrorInValueToString(instance, "text", null, null);
  }

  @Test
  public void valueToStringTime() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance();
    Calendar dateTime = Calendar.getInstance();

    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT-11:30"));
    dateTime.set(Calendar.HOUR_OF_DAY, 23);
    dateTime.set(Calendar.MINUTE, 32);
    dateTime.set(Calendar.SECOND, 3);
    assertEquals("PT23H32M3S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("PT23H32M3S", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("time'PT23H32M3S'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    dateTime.add(Calendar.MILLISECOND, 1);
    assertEquals("PT23H32M3.001S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, null));
    assertEquals("PT23H32M3.001S", instance.valueToString(dateTime, EdmLiteralKind.JSON, null));
    assertEquals("time'PT23H32M3.001S'", instance.valueToString(dateTime, EdmLiteralKind.URI, null));

    final Long millis = 84723007L;
    assertEquals("PT23H32M3.007S", instance.valueToString(millis, EdmLiteralKind.DEFAULT, null));
    assertEquals("PT23H32M3.007S", instance.valueToString(millis, EdmLiteralKind.JSON, null));
    assertEquals("time'PT23H32M3.007S'", instance.valueToString(millis, EdmLiteralKind.URI, null));

    assertTrue(instance.valueToString(new Date(millis), EdmLiteralKind.DEFAULT, null).contains("M3.007S"));

    dateTime.add(Calendar.MILLISECOND, -1);
    assertEquals("PT23H32M3S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null)));
    assertEquals("PT23H32M3.0S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null)));
    dateTime.add(Calendar.MILLISECOND, 10);
    assertEquals("PT23H32M3.01S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null)));
    dateTime.add(Calendar.MILLISECOND, -23);
    assertEquals("PT23H32M2.987S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, null)));
    assertEquals("PT23H32M2.98700S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null)));
    dateTime.add(Calendar.MILLISECOND, -87);
    assertEquals("PT23H32M2.9S", instance.valueToString(dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null)));

    expectErrorInValueToString(instance, dateTime, EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null));
    expectErrorInValueToString(instance, 0, EdmLiteralKind.DEFAULT, null);
    expectErrorInValueToString(instance, dateTime, null, null);
  }

  private void expectErrorInValueOfString(final EdmSimpleType instance, final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      instance.valueOfString(value, literalKind, facets, instance.getDefaultType());
      fail("Expected exception not thrown");
    } catch (EdmSimpleTypeException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void checkValueOfNull() throws Exception {
    for (EdmSimpleTypeKind kind : EdmSimpleTypeKind.values()) {
      if (kind == EdmSimpleTypeKind.Null)
        continue;
      final EdmSimpleType instance = kind.getEdmSimpleTypeInstance();
      assertNull(instance.valueOfString(null, EdmLiteralKind.DEFAULT, null, instance.getDefaultType()));
      assertNull(instance.valueOfString(null, EdmLiteralKind.DEFAULT, getNullableFacets(true), instance.getDefaultType()));
      assertNull(instance.valueOfString(null, EdmLiteralKind.DEFAULT, getNullableFacets(null), instance.getDefaultType()));

      expectErrorInValueOfString(instance, null, EdmLiteralKind.DEFAULT, getNullableFacets(false));
      expectErrorInValueOfString(instance, "", null, null);
    }
  }

  @Test
  public void valueOfStringBinary() throws Exception {
    final byte[] binary = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF };
    final EdmSimpleType instance = EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance();

    assertTrue(Arrays.equals(binary, instance.valueOfString("qrvM3e7/", EdmLiteralKind.DEFAULT, null, byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("qrvM3e7/", EdmLiteralKind.JSON, null, byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("binary'AABBCCDDEEFF'", EdmLiteralKind.URI, null, byte[].class)));

    assertTrue(Arrays.equals(binary, instance.valueOfString("qrvM3e7/", EdmLiteralKind.DEFAULT, getMaxLengthFacets(6), byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("qrvM3e7/", EdmLiteralKind.JSON, getMaxLengthFacets(6), byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("binary'AABBCCDDEEFF'", EdmLiteralKind.URI, getMaxLengthFacets(6), byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("X'AABBCCDDEEFF'", EdmLiteralKind.URI, getMaxLengthFacets(6), byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("qrvM3e7/", EdmLiteralKind.DEFAULT, getMaxLengthFacets(null), byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("qrvM3e7/", EdmLiteralKind.JSON, getMaxLengthFacets(null), byte[].class)));
    assertTrue(Arrays.equals(binary, instance.valueOfString("X'AABBCCDDEEFF'", EdmLiteralKind.URI, getMaxLengthFacets(null), byte[].class)));

    expectErrorInValueOfString(instance, "qrvM3e7/", EdmLiteralKind.DEFAULT, getMaxLengthFacets(3));
    expectErrorInValueOfString(instance, "qrvM3e7/", EdmLiteralKind.JSON, getMaxLengthFacets(3));
    expectErrorInValueOfString(instance, "binary'AABBCCDDEEFF'", EdmLiteralKind.URI, getMaxLengthFacets(3));

    expectErrorInValueOfString(instance, "@", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "@", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "binary'ZZ'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "Y'AA'", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringBoolean() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance();

    assertEquals(true, instance.valueOfString("true", EdmLiteralKind.DEFAULT, null, Boolean.class));
    assertEquals(false, instance.valueOfString("false", EdmLiteralKind.JSON, null, Boolean.class));
    assertEquals(true, instance.valueOfString("1", EdmLiteralKind.URI, null, Boolean.class));
    assertEquals(false, instance.valueOfString("0", EdmLiteralKind.URI, null, Boolean.class));

    expectErrorInValueOfString(instance, "True", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "-1", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "FALSE", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringByte() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance();

    assertEquals(Byte.valueOf((byte) 1), instance.valueOfString("1", EdmLiteralKind.DEFAULT, null, Byte.class));
    assertEquals(Byte.valueOf((byte) 2), instance.valueOfString("2", EdmLiteralKind.JSON, null, Byte.class));
    assertEquals(Byte.valueOf((byte) 127), instance.valueOfString("127", EdmLiteralKind.URI, null, Byte.class));
    assertEquals(Short.valueOf((short) 255), instance.valueOfString("255", EdmLiteralKind.URI, null, Short.class));

    expectErrorInValueOfString(instance, "256", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "-1", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "1.0", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringDateTime() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance();
    Calendar dateTime = Calendar.getInstance();

    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
    dateTime.set(2012, 1, 29, 23, 32, 3);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:03", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:03", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("\\/Date(1330558323000)\\/", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("datetime'2012-02-29T23%3A32%3A03'", EdmLiteralKind.URI, null, Calendar.class));

    dateTime.add(Calendar.MILLISECOND, 1);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:03.001", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("\\/Date(1330558323001)\\/", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("datetime'2012-02-29T23%3A32%3A03.001'", EdmLiteralKind.URI, null, Calendar.class));

    dateTime.add(Calendar.MILLISECOND, 9);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:03.01", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null), Calendar.class));
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:03.0100000", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, -10);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:03.000", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, -13);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:02.987", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, null), Calendar.class));
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:02.98700", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, 3);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:02.99", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, -90);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32:02.9", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, -2900);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T23:32", EdmLiteralKind.DEFAULT, null, Calendar.class));

    expectErrorInValueOfString(instance, "2012-02-29T23:32:02.9", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null));
    expectErrorInValueOfString(instance, "2012-02-29T23:32:02.98700", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null));
    expectErrorInValueOfString(instance, "2012-02-29T23:32:02.9876", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "20120229T233202", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1900-02-29T00:00:00", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "2012-02-29T24:00:01", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "/Date(1)/", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "\\/Date(12345678901234567890)\\/", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "\\/Date(1330558323000+0060)\\/", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "datetime'2012-02-29T23%3A32%3A02+01%3A00'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "date'2012-02-29T23%3A32%3A02'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "datetime'2012-02-29T23%3A32%3A02", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "datetime'", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringDateTimeOffset() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance();
    Calendar dateTime = Calendar.getInstance();

    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
    dateTime.set(2012, 1, 29, 1, 2, 3);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T01:02:03Z", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("2012-02-29T01:02:03+00:00", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("2012-02-29T01:02:03", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("\\/Date(1330477323000)\\/", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("\\/Date(1330477323000-0000)\\/", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("datetimeoffset'2012-02-29T01%3A02%3A03Z'", EdmLiteralKind.URI, null, Calendar.class));

    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT-01:30"));
    dateTime.set(2012, 1, 29, 1, 2, 3);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T01:02:03-01:30", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("\\/Date(1330477323000-0090)\\/", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("datetimeoffset'2012-02-29T01%3A02%3A03-01%3A30'", EdmLiteralKind.URI, null, Calendar.class));

    dateTime.clear();
    dateTime.setTimeZone(TimeZone.getTimeZone("GMT+11:00"));
    dateTime.set(2012, 1, 29, 1, 2, 3);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T01:02:03+11:00", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("\\/Date(1330477323000+0660)\\/", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("datetimeoffset'2012-02-29T01%3A02%3A03+11%3A00'", EdmLiteralKind.URI, null, Calendar.class));

    dateTime.add(Calendar.MILLISECOND, 7);
    assertEquals(dateTime, instance.valueOfString("2012-02-29T01:02:03.007+11:00", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("\\/Date(1330477323007+0660)\\/", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("datetimeoffset'2012-02-29T01%3A02%3A03.007+11%3A00'", EdmLiteralKind.URI, null, Calendar.class));

    expectErrorInValueOfString(instance, "2012-02-29T23:32:02.9Z", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null));
    expectErrorInValueOfString(instance, "datetime'2012-02-29T23%3A32%3A02'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "2012-02-29T23:32:02X", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "2012-02-29T23:32:02+24:00", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "\\/Date(12345678901234567890)\\/", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "\\/Date(1234567890-1440)\\/", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "\\/Date(1234567890Z)\\/", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "datetimeoffset'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "datetimeoffset''Z", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringDecimal() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance();

    assertEquals(BigDecimal.ONE, instance.valueOfString("1", EdmLiteralKind.DEFAULT, null, BigDecimal.class));
    assertEquals(new BigDecimal(-2), instance.valueOfString("-2", EdmLiteralKind.JSON, null, BigDecimal.class));
    assertEquals(new BigDecimal("-12345678901234567890"), instance.valueOfString("-12345678901234567890M", EdmLiteralKind.URI, null, BigDecimal.class));
    assertEquals(BigDecimal.ZERO, instance.valueOfString("0M", EdmLiteralKind.URI, null, BigDecimal.class));

    assertEquals(new BigDecimal(-32768), instance.valueOfString("-32768", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(42, null), BigDecimal.class));
    assertEquals(new BigDecimal(-32768), instance.valueOfString("-32768", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null), BigDecimal.class));
    assertEquals(new BigDecimal(32768), instance.valueOfString("32768", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null), BigDecimal.class));
    assertEquals(new BigDecimal(0.5), instance.valueOfString("0.5", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null), BigDecimal.class));
    assertEquals(new BigDecimal(0.5), instance.valueOfString("0.5", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, 1), BigDecimal.class));
    assertEquals(new BigDecimal("12.3"), instance.valueOfString("12.3", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(3, 1), BigDecimal.class));
    expectErrorInValueOfString(instance, "-1234", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null));
    expectErrorInValueOfString(instance, "1234", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(3, null));
    expectErrorInValueOfString(instance, "12.34", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(3, null));
    expectErrorInValueOfString(instance, "12.34", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(3, 2));
    expectErrorInValueOfString(instance, "12.34", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(4, 1));
    expectErrorInValueOfString(instance, "0.00390625", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null));
    expectErrorInValueOfString(instance, "0.00390625", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, 7));

    expectErrorInValueOfString(instance, "-1E2", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1.", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, ".1", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1.0.1", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1M", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "0", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "1.0D", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "0F", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "0x42", EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueOfStringDouble() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance();

    assertEquals(Double.valueOf(1.42), instance.valueOfString("1.42", EdmLiteralKind.DEFAULT, null, Double.class));
    assertEquals(Double.valueOf(-42.42), instance.valueOfString("-42.42", EdmLiteralKind.JSON, null, Double.class));
    assertEquals(Double.valueOf(42.0), instance.valueOfString("42D", EdmLiteralKind.URI, null, Double.class));
    assertEquals(Double.valueOf(42E42), instance.valueOfString("42E42d", EdmLiteralKind.URI, null, Double.class));

    assertEquals(Double.valueOf(Double.NaN), instance.valueOfString("NaN", EdmLiteralKind.DEFAULT, null, Double.class));
    assertEquals(Double.valueOf(Double.NEGATIVE_INFINITY), instance.valueOfString("-INF", EdmLiteralKind.JSON, null, Double.class));
    assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), instance.valueOfString("INF", EdmLiteralKind.URI, null, Double.class));

    expectErrorInValueOfString(instance, "42E400", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "42.42.42", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "42.42.42D", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "42F", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "42", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "0x42P42", EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueOfStringGuid() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance();
    final UUID uuid = UUID.fromString("aabbccdd-aabb-ccdd-eeff-aabbccddeeff");

    assertEquals(uuid, instance.valueOfString("aabbccdd-aabb-ccdd-eeff-aabbccddeeff", EdmLiteralKind.DEFAULT, null, UUID.class));
    assertEquals(uuid, instance.valueOfString("AABBCCDD-AABB-CCDD-EEFF-AABBCCDDEEFF", EdmLiteralKind.JSON, null, UUID.class));
    assertEquals(uuid, instance.valueOfString("guid'AABBCCDD-aabb-ccdd-eeff-AABBCCDDEEFF'", EdmLiteralKind.URI, null, UUID.class));

    expectErrorInValueOfString(instance, "AABBCCDDAABBCCDDEEFFAABBCCDDEEFF", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "uid'AABBCCDD-aabb-ccdd-eeff-AABBCCDDEEFF'", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringInt16() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance();

    assertEquals(Short.valueOf((short) 1), instance.valueOfString("1", EdmLiteralKind.DEFAULT, null, Short.class));
    assertEquals(Short.valueOf((short) 2), instance.valueOfString("2", EdmLiteralKind.JSON, null, Short.class));
    assertEquals(Short.valueOf((short) -32768), instance.valueOfString("-32768", EdmLiteralKind.URI, null, Short.class));
    assertEquals(Short.valueOf((short) 32767), instance.valueOfString("32767", EdmLiteralKind.URI, null, Short.class));

    expectErrorInValueOfString(instance, "32768", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1.0", EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueOfStringInt32() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance();

    assertEquals(Byte.valueOf((byte) 1), instance.valueOfString("1", EdmLiteralKind.DEFAULT, null, Byte.class));
    assertEquals(Short.valueOf((short) 2), instance.valueOfString("2", EdmLiteralKind.JSON, null, Short.class));
    assertEquals(Integer.valueOf(-10000000), instance.valueOfString("-10000000", EdmLiteralKind.URI, null, Integer.class));
    assertEquals(Long.valueOf(10000000), instance.valueOfString("10000000", EdmLiteralKind.URI, null, Long.class));

    expectErrorInValueOfString(instance, "-2147483649", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1.0", EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueOfStringInt64() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance();

    assertEquals(Long.valueOf(1), instance.valueOfString("1", EdmLiteralKind.DEFAULT, null, Long.class));
    assertEquals(Long.valueOf(2), instance.valueOfString("2", EdmLiteralKind.JSON, null, Long.class));
    assertEquals(Long.valueOf(-1234567890123456789L), instance.valueOfString("-1234567890123456789L", EdmLiteralKind.URI, null, Long.class));
    assertEquals(Long.valueOf(0), instance.valueOfString("0l", EdmLiteralKind.URI, null, Long.class));

    expectErrorInValueOfString(instance, "-12345678901234567890", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1.0", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "1.0L", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "0M", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "0x42", EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueOfStringSByte() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance();

    assertEquals(Byte.valueOf((byte) 1), instance.valueOfString("1", EdmLiteralKind.DEFAULT, null, Byte.class));
    assertEquals(Byte.valueOf((byte) -2), instance.valueOfString("-2", EdmLiteralKind.JSON, null, Byte.class));
    assertEquals(Byte.valueOf((byte) 127), instance.valueOfString("127", EdmLiteralKind.URI, null, Byte.class));
    assertEquals(Byte.valueOf((byte) -128), instance.valueOfString("-128", EdmLiteralKind.URI, null, Byte.class));

    expectErrorInValueOfString(instance, "128", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "-129", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "1.0", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringSingle() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance();

    assertEquals(Float.valueOf(1.42f), instance.valueOfString("1.42", EdmLiteralKind.DEFAULT, null, Float.class));
    assertEquals(Float.valueOf(-42.42f), instance.valueOfString("-42.42", EdmLiteralKind.JSON, null, Float.class));
    assertEquals(Float.valueOf(42.0f), instance.valueOfString("42F", EdmLiteralKind.URI, null, Float.class));
    assertEquals(Float.valueOf(2.2E38f), instance.valueOfString("22E37f", EdmLiteralKind.URI, null, Float.class));

    assertEquals(Float.valueOf(Float.NaN), instance.valueOfString("NaN", EdmLiteralKind.DEFAULT, null, Float.class));
    assertEquals(Float.valueOf(Float.NEGATIVE_INFINITY), instance.valueOfString("-INF", EdmLiteralKind.JSON, null, Float.class));
    assertEquals(Float.valueOf(Float.POSITIVE_INFINITY), instance.valueOfString("INF", EdmLiteralKind.URI, null, Float.class));

    expectErrorInValueOfString(instance, "42E42", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "42.42.42", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "42.42.42F", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "42D", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "42", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "0x42P4", EdmLiteralKind.DEFAULT, null);
  }

  @Test
  public void valueOfStringString() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.String.getEdmSimpleTypeInstance();

    assertEquals("text", instance.valueOfString("text", EdmLiteralKind.DEFAULT, null, String.class));
    assertEquals("a\nb", instance.valueOfString("a\nb", EdmLiteralKind.JSON, null, String.class));
    assertEquals("true", instance.valueOfString("'true'", EdmLiteralKind.URI, null, String.class));
    assertEquals("a'b", instance.valueOfString("'a''b'", EdmLiteralKind.URI, null, String.class));

    assertEquals("text", instance.valueOfString("text", EdmLiteralKind.DEFAULT, getUnicodeFacets(true), String.class));
    assertEquals("text", instance.valueOfString("text", EdmLiteralKind.DEFAULT, getUnicodeFacets(null), String.class));
    assertEquals("text", instance.valueOfString("text", EdmLiteralKind.DEFAULT, getMaxLengthFacets(4), String.class));
    assertEquals("text", instance.valueOfString("text", EdmLiteralKind.DEFAULT, getMaxLengthFacets(null), String.class));

    expectErrorInValueOfString(instance, "schräg", EdmLiteralKind.DEFAULT, getUnicodeFacets(false));
    expectErrorInValueOfString(instance, "text", EdmLiteralKind.DEFAULT, getMaxLengthFacets(3));
    expectErrorInValueOfString(instance, "'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "'text", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "text'", EdmLiteralKind.URI, null);
  }

  @Test
  public void valueOfStringTime() throws Exception {
    final EdmSimpleType instance = EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance();
    Calendar dateTime = Calendar.getInstance();

    dateTime.clear();
    dateTime.set(Calendar.HOUR_OF_DAY, 23);
    dateTime.set(Calendar.MINUTE, 32);
    dateTime.set(Calendar.SECOND, 3);
    assertEquals(dateTime, instance.valueOfString("PT23H32M3S", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("PT84723S", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("PT23H32M3S", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("time'PT23H32M3S'", EdmLiteralKind.URI, null, Calendar.class));

    dateTime.add(Calendar.MILLISECOND, 1);
    assertEquals(dateTime, instance.valueOfString("PT23H32M3.001S", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("PT23H32M3.001S", EdmLiteralKind.JSON, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("time'PT23H32M3.001S'", EdmLiteralKind.URI, null, Calendar.class));

    dateTime.add(Calendar.MILLISECOND, -1);
    assertEquals(dateTime, instance.valueOfString("PT23H32M3S", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, 10);
    assertEquals(dateTime, instance.valueOfString("PT23H32M3.01S", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(2, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, -23);
    assertEquals(dateTime, instance.valueOfString("PT23H32M2.987S", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(null, null), Calendar.class));
    assertEquals(dateTime, instance.valueOfString("PT23H32M2.98700S", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(5, null), Calendar.class));
    dateTime.add(Calendar.MILLISECOND, -87);
    assertEquals(dateTime, instance.valueOfString("PT23H32M2.9S", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(1, null), Calendar.class));

    dateTime.add(Calendar.HOUR, -23);
    assertEquals(dateTime, instance.valueOfString("PT32M2.9S", EdmLiteralKind.DEFAULT, null, Calendar.class));
    dateTime.add(Calendar.MINUTE, -32);
    assertEquals(dateTime, instance.valueOfString("PT2.9S", EdmLiteralKind.DEFAULT, null, Calendar.class));
    assertEquals(dateTime, instance.valueOfString("PT2.900S", EdmLiteralKind.JSON, null, Calendar.class));
    dateTime.add(Calendar.MILLISECOND, -2900);
    assertEquals(dateTime, instance.valueOfString("PT0S", EdmLiteralKind.DEFAULT, null, Calendar.class));
    dateTime.add(Calendar.MINUTE, 59);
    assertEquals(dateTime, instance.valueOfString("PT59M", EdmLiteralKind.DEFAULT, null, Calendar.class));

    expectErrorInValueOfString(instance, "PT1H2M3.1234S", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "PT13H2M3.9S", EdmLiteralKind.DEFAULT, getPrecisionScaleFacets(0, null));
    expectErrorInValueOfString(instance, "P2012Y2M29DT23H32M2S", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "PT24H", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "PT99999S", EdmLiteralKind.JSON, null);
    expectErrorInValueOfString(instance, "PT999H", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "PT", EdmLiteralKind.DEFAULT, null);
    expectErrorInValueOfString(instance, "datetime'PT23H32M2S'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "time'", EdmLiteralKind.URI, null);
    expectErrorInValueOfString(instance, "time''PT", EdmLiteralKind.URI, null);
  }

  @Test
  public void validate() throws Exception {
    for (EdmSimpleTypeKind kind : EdmSimpleTypeKind.values()) {
      if (kind == EdmSimpleTypeKind.Null)
        continue;
      final EdmSimpleType instance = kind.getEdmSimpleTypeInstance();
      assertTrue(instance.validate(null, null, null));
      assertTrue(instance.validate(null, null, getNullableFacets(true)));
      assertFalse(instance.validate(null, null, getNullableFacets(false)));
      assertFalse(instance.validate("", null, null));
      assertFalse(instance.validate("ä", EdmLiteralKind.DEFAULT, getUnicodeFacets(false)));
      assertFalse(instance.validate("ä", EdmLiteralKind.URI, null));
    }
  }
}