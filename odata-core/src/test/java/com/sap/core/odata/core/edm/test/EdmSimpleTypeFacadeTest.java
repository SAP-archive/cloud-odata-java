package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.uri.EdmLiteral;
import com.sap.core.odata.api.uri.UriSyntaxException;
import com.sap.core.odata.core.edm.Bit;
import com.sap.core.odata.core.edm.EdmBinary;
import com.sap.core.odata.core.edm.EdmBoolean;
import com.sap.core.odata.core.edm.EdmByte;
import com.sap.core.odata.core.edm.EdmDateTime;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.edm.EdmDecimal;
import com.sap.core.odata.core.edm.EdmDouble;
import com.sap.core.odata.core.edm.EdmGuid;
import com.sap.core.odata.core.edm.EdmInt16;
import com.sap.core.odata.core.edm.EdmInt32;
import com.sap.core.odata.core.edm.EdmInt64;
import com.sap.core.odata.core.edm.EdmSByte;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;
import com.sap.core.odata.core.edm.EdmSingle;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.core.edm.EdmTime;
import com.sap.core.odata.core.edm.Uint7;

public class EdmSimpleTypeFacadeTest {

  public EdmSimpleType parse(String literal) throws UriSyntaxException {
    EdmLiteral uriLiteral = EdmSimpleTypeKind.parseUriLiteral(literal);
    return uriLiteral.getType();
  }

  public void compare(EdmSimpleTypeKind simpleType) {
    EdmSimpleType bin1 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(simpleType);
    assertNotNull(bin1);

    EdmSimpleType bin2 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(simpleType);
    assertNotNull(bin2);

    assertTrue(bin1.equals(bin2));
  }

  @Test
  public void parseUriLiteralBinary() throws UriSyntaxException {
    EdmSimpleType binary = parse("X'Fa12aAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmBinary);

    binary = parse("binary'FA12AAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmBinary);

  }

  @Test
  public void parseUriLiteralBoolean() throws UriSyntaxException {
    EdmSimpleType bool = parse("true");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmBoolean);

    bool = parse("false");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmBoolean);
  }

  @Test
  public void parseUriLiteralBit() throws UriSyntaxException {
    EdmSimpleType bit = parse("1");
    assertNotNull(bit);
    assertTrue(bit instanceof Bit);

    bit = parse("0");
    assertNotNull(bit);
    assertTrue(bit instanceof Bit);
  }

  @Test
  public void parseUriLiteralByte() throws UriSyntaxException {
    EdmSimpleType byt = parse("255");
    assertNotNull(byt);
    assertTrue(byt instanceof EdmByte);
  }

  @Test
  public void parseUriLiteralUint7() throws UriSyntaxException {
    EdmSimpleType uInt7 = parse("123");
    assertNotNull(uInt7);
    assertTrue(uInt7 instanceof Uint7);
  }

  @Test
  public void parseUriLiteralDateTime() throws UriSyntaxException {
    EdmSimpleType dt = parse("datetime'2009-12-26T21%3A23%3A38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmDateTime);

    dt = parse("datetime'2009-12-26T21%3A23%3A38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmDateTime);
  }

  @Test
  public void parseUriLiteralDateTimeOffset() throws UriSyntaxException {
    EdmSimpleType dto = parse("datetimeoffset'2009-12-26T21%3A23%3A38Z'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmDateTimeOffset);

    dto = parse("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmDateTimeOffset);
  }

  @Test
  public void parseUriLiteralDecimal() throws UriSyntaxException {
    EdmSimpleType dec = parse("4.5m");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmDecimal);

    dec = parse("4.5M");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmDecimal);
  }

  @Test
  public void parseUriLiteralDouble() throws UriSyntaxException {
    EdmSimpleType doub = parse("4.5d");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmDouble);

    doub = parse("4.5D");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmDouble);
  }

  @Test
  public void parseUriLiteralGuid() throws UriSyntaxException {
    EdmSimpleType guid = parse("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'");
    assertNotNull(guid);
    assertTrue(guid instanceof EdmGuid);
  }

  @Test
  public void parseUriLiteralInt16() throws UriSyntaxException {
    EdmSimpleType in = parse("-32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt16);

    in = parse("3276");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt16);
  }

  @Test
  public void parseUriLiteralInt32() throws UriSyntaxException {
    EdmSimpleType in = parse("-327687");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt32);

    in = parse("32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt32);
  }

  @Test
  public void parseUriLiteralInt64() throws UriSyntaxException {
    EdmSimpleType in = parse("64L");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt64);

    in = parse("64l");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt64);
  }

  @Test
  public void parseUriLiteralNull() throws UriSyntaxException {

  }

  @Test
  public void parseUriLiteralSByte() throws UriSyntaxException {
    EdmSimpleType sb = parse("-123");
    assertNotNull(sb);
    assertTrue(sb instanceof EdmSByte);
  }

  @Test
  public void parseUriLiteralSingle() throws UriSyntaxException {
    EdmSimpleType sing = parse("4.5f");
    assertNotNull(sing);
    assertTrue(sing instanceof EdmSingle);
  }

  @Test
  public void parseUriLiteralString() throws UriSyntaxException {
    EdmSimpleType str = parse("'abc'");
    assertNotNull(str);
    assertTrue(str instanceof EdmString);
  }

  @Test
  public void parseUriLiteralTime() throws UriSyntaxException {
    EdmSimpleType time = parse("time'P120D'");
    assertNotNull(time);
    assertTrue(time instanceof EdmTime);
  }

  @Test
  public void compareTypes() {
    compare(EdmSimpleTypeKind.Binary);
    compare(EdmSimpleTypeKind.Boolean);
    compare(EdmSimpleTypeKind.Byte);
    compare(EdmSimpleTypeKind.SByte);
    compare(EdmSimpleTypeKind.DateTime);
    compare(EdmSimpleTypeKind.DateTimeOffset);
    compare(EdmSimpleTypeKind.Decimal);
    compare(EdmSimpleTypeKind.Double);
    compare(EdmSimpleTypeKind.Guid);
    compare(EdmSimpleTypeKind.Int16);
    compare(EdmSimpleTypeKind.Int32);
    compare(EdmSimpleTypeKind.Int64);
    compare(EdmSimpleTypeKind.Single);
    compare(EdmSimpleTypeKind.Time);
  }

  /**
   * Parse a URI literal value string and assert that it is compatible
   * to the given EDM simple type and has the correct parsed value.
   * 
   * @param literal
   *          the URI literal value to be parsed as string
   * @param type
   *          the {@link EdmSimpleType} the URI literal should be compatible to
   * @param expectedLiteral
   *          the expected literal value
   * @throws UriSyntaxException 
   * @throws EdmException
   */
  private void parseLiteral(final String literal, final EdmSimpleType type, final String expectedLiteral) throws UriSyntaxException, EdmException {
    final EdmLiteral uriLiteral = EdmSimpleTypeKind.parseUriLiteral(literal);

    assertTrue(type.isCompatible(uriLiteral.getType()));
    assertEquals(expectedLiteral, uriLiteral.getLiteral());
  }

  @Test
  public void parseDecimal() throws Exception {
    parseLiteral("4.5m", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "4.5");
    parseLiteral("4.5M", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "4.5");
    parseLiteral("1", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "1");
    parseLiteral("255", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "-32768");
    parseLiteral("32768", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "32768");
    parseLiteral("3000000", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "3000000");
    parseLiteral("4.5d", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "4.5");
    parseLiteral("4.2E9F", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "4.2E9");
    parseLiteral("1234567890", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance(), "1234567890");
  }

  @Test
  public void parseInt16() throws Exception {
    parseLiteral("16", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(), "16");
    parseLiteral("-16", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(), "-16");
    parseLiteral("255", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(), "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(), "-32768");

  }

  @Test
  public void parseInt32() throws Exception {
    parseLiteral("32", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(), "32");
    parseLiteral("-127", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(), "-127");
    parseLiteral("255", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(), "255");
    parseLiteral("32767", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(), "32767");
    parseLiteral("-32769", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance(), "-32769");
  }

  @Test
  public void parseInt64() throws Exception {
    parseLiteral("64", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(), "64");
    parseLiteral("255", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(), "255");
    parseLiteral("1000", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(), "1000");
    parseLiteral("100000", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(), "100000");
    parseLiteral("-64L", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(), "-64");
    parseLiteral("" + Long.MAX_VALUE + "L", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(), "" + Long.MAX_VALUE);
    parseLiteral("" + Long.MIN_VALUE + "l", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance(), "" + Long.MIN_VALUE);
  }

  @Test
  public void parseString() throws Exception {
    parseLiteral("'abc'", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(), "abc");
    parseLiteral("'true'", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(), "true");
    parseLiteral("''", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(), "");
  }

  @Test
  public void parseSingle() throws Exception {
    parseLiteral("45", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "45");
    parseLiteral("255", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "-32768");
    parseLiteral("32768", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "32768");
    parseLiteral("1L", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "1");
    parseLiteral("4.5f", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "4.5");
    parseLiteral("4.5F", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "4.5");
    parseLiteral("4.5e9f", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance(), "4.5e9");
  }

  @Test
  public void parseDouble() throws Exception {
    parseLiteral("45", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "45");
    parseLiteral("255", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "-32768");
    parseLiteral("32768", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "32768");
    parseLiteral("1l", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "1");
    parseLiteral("4.5d", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "4.5");
    parseLiteral("4.5D", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "4.5");
    parseLiteral("4.5e21f", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance(), "4.5e21");
  }

  @Test
  public void parseByte() throws Exception {
    parseLiteral("255", EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(), "255");
    parseLiteral("123", EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance(), "123");
  }

  @Test
  public void parseGuid() throws Exception {
    parseLiteral("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'", EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance(), "1225c695-cfb8-4ebb-aaaa-80da344efa6a");
  }

  @Test
  public void parseTime() throws Exception {
    parseLiteral("time'P120D'", EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance(), "P120D");
  }

  @Test
  public void parseDatetime() throws Exception {
    parseLiteral("datetime'2009-12-26T21:23:38'", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance(), "2009-12-26T21:23:38");
    parseLiteral("datetime'2009-12-26T21:23:38Z'", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance(), "2009-12-26T21:23:38Z");
  }

  @Test
  public void parseDatetimeOffset() throws Exception {
    parseLiteral("datetimeoffset'2009-12-26T21:23:38Z'", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance(), "2009-12-26T21:23:38Z");
    parseLiteral("datetimeoffset'2002-10-10T12:00:00-05:00'", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance(), "2002-10-10T12:00:00-05:00");
  }

  @Test
  public void parseBoolean() throws Exception {
    parseLiteral("true", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(), "true");
    parseLiteral("false", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(), "false");
    parseLiteral("1", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(), "1");
    parseLiteral("0", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance(), "0");
  }

  @Test
  public void parseSByte() throws Exception {
    parseLiteral("-123", EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(), "-123");
    parseLiteral("12", EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance(), "12");
  }

  @Test
  public void parseBinary() throws Exception {
    parseLiteral("X'Fa12aAA1'", EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance(), "+hKqoQ==");
    parseLiteral("binary'FA12AAA1'", EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance(), "+hKqoQ==");
  }

  private void parseWrongLiteralContent(final String literal) {
    try {
      EdmSimpleTypeKind.parseUriLiteral(literal);
      fail("Expected UriParserException not thrown");
    } catch (UriSyntaxException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void parseLiteralWithWrongContent() throws Exception {
    parseWrongLiteralContent("binary'abcde'");
    parseWrongLiteralContent("'");
    parseWrongLiteralContent("'a");
    parseWrongLiteralContent("wrongprefix'PT1H2M3S'");
    parseWrongLiteralContent("32i");
    parseWrongLiteralContent("12345678901234567890");
  }

  private void parseIncompatibleLiteralContent(final String literal, final EdmSimpleType type) throws UriSyntaxException {
    final EdmLiteral uriLiteral = EdmSimpleTypeKind.parseUriLiteral(literal);
    assertFalse(type.isCompatible(uriLiteral.getType()));
  }

  @Test
  public void parseIncompatibleLiteral() throws Exception {
    parseIncompatibleLiteralContent("1D", EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("'0'", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("'1'", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("2", EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("-1", EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("-129", EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("time'PT11H12M13S'", EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("time'PT11H12M13S'", EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("'1'", EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("1M", EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("1", EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("32768", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("1L", EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("1M", EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("128", EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("1D", EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("1", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
    parseIncompatibleLiteralContent("datetime'2012-10-10T11:12:13'", EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance());
  }
}
