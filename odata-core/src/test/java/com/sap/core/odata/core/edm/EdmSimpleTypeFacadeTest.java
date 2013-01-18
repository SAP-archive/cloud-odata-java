package com.sap.core.odata.core.edm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.uri.UriSyntaxException;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class EdmSimpleTypeFacadeTest extends BaseTest {

  public EdmSimpleType parse(final String literal) throws EdmLiteralException {
    EdmLiteral uriLiteral = EdmSimpleTypeKind.parseUriLiteral(literal);
    return uriLiteral.getType();
  }

  public void compare(final EdmSimpleTypeKind simpleType) {
    EdmSimpleType bin1 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(simpleType);
    assertNotNull(bin1);

    EdmSimpleType bin2 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(simpleType);
    assertNotNull(bin2);

    assertEquals(bin1, bin2);
  }

  @Test
  public void parseUriLiteralBinary() throws EdmLiteralException {
    EdmSimpleType binary = parse("X'Fa12aAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmBinary);

    binary = parse("binary'FA12AAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmBinary);
  }

  @Test
  public void parseUriLiteralBoolean() throws EdmLiteralException {
    EdmSimpleType bool = parse("true");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmBoolean);

    bool = parse("false");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmBoolean);
  }

  @Test
  public void parseUriLiteralBit() throws EdmLiteralException {
    EdmSimpleType bit = parse("1");
    assertNotNull(bit);
    assertTrue(bit instanceof Bit);

    bit = parse("0");
    assertNotNull(bit);
    assertTrue(bit instanceof Bit);
  }

  @Test
  public void parseUriLiteralByte() throws EdmLiteralException {
    EdmSimpleType byt = parse("255");
    assertNotNull(byt);
    assertTrue(byt instanceof EdmByte);
  }

  @Test
  public void parseUriLiteralUint7() throws EdmLiteralException {
    EdmSimpleType uInt7 = parse("123");
    assertNotNull(uInt7);
    assertTrue(uInt7 instanceof Uint7);

    uInt7 = parse("111");
    assertNotNull(uInt7);
    assertTrue(uInt7 instanceof Uint7);

    uInt7 = parse("42");
    assertNotNull(uInt7);
    assertTrue(uInt7 instanceof Uint7);
}

  @Test
  public void parseUriLiteralDateTime() throws EdmLiteralException {
    EdmSimpleType dt = parse("datetime'2009-12-26T21:23:38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmDateTime);

    dt = parse("datetime'2009-12-26T21:23:38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmDateTime);
  }

  @Test
  public void parseUriLiteralDateTimeOffset() throws EdmLiteralException {
    EdmSimpleType dto = parse("datetimeoffset'2009-12-26T21:23:38Z'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmDateTimeOffset);

    dto = parse("datetimeoffset'2002-10-10T12:00:00-05:00'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmDateTimeOffset);
  }

  @Test
  public void parseUriLiteralDecimal() throws EdmLiteralException {
    EdmSimpleType dec = parse("4.5m");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmDecimal);

    dec = parse("4.5M");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmDecimal);
  }

  @Test
  public void parseUriLiteralDouble() throws EdmLiteralException {
    EdmSimpleType doub = parse("4.5d");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmDouble);

    doub = parse("4.5D");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmDouble);
  }

  @Test
  public void parseUriLiteralGuid() throws EdmLiteralException {
    EdmSimpleType guid = parse("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'");
    assertNotNull(guid);
    assertTrue(guid instanceof EdmGuid);
  }

  @Test
  public void parseUriLiteralInt16() throws EdmLiteralException {
    EdmSimpleType in = parse("-32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt16);

    in = parse("3276");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt16);
  }

  @Test
  public void parseUriLiteralInt32() throws EdmLiteralException {
    EdmSimpleType in = parse("-327687");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt32);

    in = parse("32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt32);
  }

  @Test
  public void parseUriLiteralInt64() throws EdmLiteralException {
    EdmSimpleType in = parse("64L");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt64);

    in = parse("64l");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt64);
  }

  @Test
  public void parseUriLiteralNull() throws EdmLiteralException {
    EdmSimpleType in = parse("null");
    assertNotNull(in);
    assertTrue(in instanceof EdmNull);
  }

  @Test
  public void parseUriLiteralSByte() throws EdmLiteralException {
    EdmSimpleType sb = parse("-123");
    assertNotNull(sb);
    assertTrue(sb instanceof EdmSByte);
  }

  @Test
  public void parseUriLiteralSingle() throws EdmLiteralException {
    EdmSimpleType sing = parse("4.5f");
    assertNotNull(sing);
    assertTrue(sing instanceof EdmSingle);
  }

  @Test
  public void parseUriLiteralString() throws EdmLiteralException {
    EdmSimpleType str = parse("'abc'");
    assertNotNull(str);
    assertTrue(str instanceof EdmString);
  }

  @Test
  public void parseUriLiteralTime() throws EdmLiteralException {
    EdmSimpleType time = parse("time'PT120S'");
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
   * @param typeKind
   *          the {@link EdmSimpleTypeKind} the URI literal should be compatible to
   * @param expectedLiteral
   *          the expected literal value
   * @throws UriSyntaxException 
   * @throws EdmException
   */
  private void parseLiteral(final String literal, final EdmSimpleTypeKind typeKind, final String expectedLiteral) throws UriSyntaxException, EdmException {
    final EdmLiteral uriLiteral = EdmSimpleTypeKind.parseUriLiteral(literal);

    assertTrue(typeKind.getEdmSimpleTypeInstance().isCompatible(uriLiteral.getType()));
    assertEquals(expectedLiteral, uriLiteral.getLiteral());
  }

  @Test
  public void parseDecimal() throws Exception {
    parseLiteral("4.5m", EdmSimpleTypeKind.Decimal, "4.5");
    parseLiteral("4.5M", EdmSimpleTypeKind.Decimal, "4.5");
    parseLiteral("1", EdmSimpleTypeKind.Decimal, "1");
    parseLiteral("255", EdmSimpleTypeKind.Decimal, "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Decimal, "-32768");
    parseLiteral("32768", EdmSimpleTypeKind.Decimal, "32768");
    parseLiteral("3000000", EdmSimpleTypeKind.Decimal, "3000000");
    parseLiteral("4.5d", EdmSimpleTypeKind.Decimal, "4.5");
    parseLiteral("4.2E9F", EdmSimpleTypeKind.Decimal, "4.2E9");
    parseLiteral("1234567890", EdmSimpleTypeKind.Decimal, "1234567890");
  }

  @Test
  public void parseInt16() throws Exception {
    parseLiteral("16", EdmSimpleTypeKind.Int16, "16");
    parseLiteral("-16", EdmSimpleTypeKind.Int16, "-16");
    parseLiteral("255", EdmSimpleTypeKind.Int16, "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Int16, "-32768");

  }

  @Test
  public void parseInt32() throws Exception {
    parseLiteral("32", EdmSimpleTypeKind.Int32, "32");
    parseLiteral("-127", EdmSimpleTypeKind.Int32, "-127");
    parseLiteral("255", EdmSimpleTypeKind.Int32, "255");
    parseLiteral("32767", EdmSimpleTypeKind.Int32, "32767");
    parseLiteral("-32769", EdmSimpleTypeKind.Int32, "-32769");
  }

  @Test
  public void parseInt64() throws Exception {
    parseLiteral("64", EdmSimpleTypeKind.Int64, "64");
    parseLiteral("255", EdmSimpleTypeKind.Int64, "255");
    parseLiteral("1000", EdmSimpleTypeKind.Int64, "1000");
    parseLiteral("100000", EdmSimpleTypeKind.Int64, "100000");
    parseLiteral("-64L", EdmSimpleTypeKind.Int64, "-64");
    parseLiteral("" + Long.MAX_VALUE + "L", EdmSimpleTypeKind.Int64, "" + Long.MAX_VALUE);
    parseLiteral("" + Long.MIN_VALUE + "l", EdmSimpleTypeKind.Int64, "" + Long.MIN_VALUE);
  }

  @Test
  public void parseString() throws Exception {
    parseLiteral("'abc'", EdmSimpleTypeKind.String, "abc");
    parseLiteral("'true'", EdmSimpleTypeKind.String, "true");
    parseLiteral("''", EdmSimpleTypeKind.String, "");
  }

  @Test
  public void parseSingle() throws Exception {
    parseLiteral("45", EdmSimpleTypeKind.Single, "45");
    parseLiteral("255", EdmSimpleTypeKind.Single, "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Single, "-32768");
    parseLiteral("32768", EdmSimpleTypeKind.Single, "32768");
    parseLiteral("1L", EdmSimpleTypeKind.Single, "1");
    parseLiteral("4.5f", EdmSimpleTypeKind.Single, "4.5");
    parseLiteral("4.5F", EdmSimpleTypeKind.Single, "4.5");
    parseLiteral("4.5e9f", EdmSimpleTypeKind.Single, "4.5e9");
  }

  @Test
  public void parseDouble() throws Exception {
    parseLiteral("45", EdmSimpleTypeKind.Double, "45");
    parseLiteral("255", EdmSimpleTypeKind.Double, "255");
    parseLiteral("-32768", EdmSimpleTypeKind.Double, "-32768");
    parseLiteral("32768", EdmSimpleTypeKind.Double, "32768");
    parseLiteral("1l", EdmSimpleTypeKind.Double, "1");
    parseLiteral("4.5d", EdmSimpleTypeKind.Double, "4.5");
    parseLiteral("4.5D", EdmSimpleTypeKind.Double, "4.5");
    parseLiteral("4.5e21f", EdmSimpleTypeKind.Double, "4.5e21");
  }

  @Test
  public void parseByte() throws Exception {
    parseLiteral("255", EdmSimpleTypeKind.Byte, "255");
    parseLiteral("123", EdmSimpleTypeKind.Byte, "123");
  }

  @Test
  public void parseGuid() throws Exception {
    parseLiteral("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'", EdmSimpleTypeKind.Guid, "1225c695-cfb8-4ebb-aaaa-80da344efa6a");
  }

  @Test
  public void parseTime() throws Exception {
    parseLiteral("time'PT120S'", EdmSimpleTypeKind.Time, "PT120S");
  }

  @Test
  public void parseDatetime() throws Exception {
    parseLiteral("datetime'2009-12-26T21:23:38'", EdmSimpleTypeKind.DateTime, "2009-12-26T21:23:38");
  }

  @Test
  public void parseDatetimeOffset() throws Exception {
    parseLiteral("datetimeoffset'2009-12-26T21:23:38Z'", EdmSimpleTypeKind.DateTimeOffset, "2009-12-26T21:23:38Z");
    parseLiteral("datetimeoffset'2002-10-10T12:00:00-05:00'", EdmSimpleTypeKind.DateTimeOffset, "2002-10-10T12:00:00-05:00");
    parseLiteral("datetimeoffset'2009-12-26T21:23:38'", EdmSimpleTypeKind.DateTimeOffset, "2009-12-26T21:23:38");
  }

  @Test
  public void parseBoolean() throws Exception {
    parseLiteral("true", EdmSimpleTypeKind.Boolean, "true");
    parseLiteral("false", EdmSimpleTypeKind.Boolean, "false");
    parseLiteral("1", EdmSimpleTypeKind.Boolean, "1");
    parseLiteral("0", EdmSimpleTypeKind.Boolean, "0");
  }

  @Test
  public void parseSByte() throws Exception {
    parseLiteral("-123", EdmSimpleTypeKind.SByte, "-123");
    parseLiteral("12", EdmSimpleTypeKind.SByte, "12");
  }

  @Test
  public void parseBinary() throws Exception {
    parseLiteral("X'Fa12aAA1'", EdmSimpleTypeKind.Binary, "+hKqoQ==");
    parseLiteral("binary'FA12AAA1'", EdmSimpleTypeKind.Binary, "+hKqoQ==");
  }

  private void parseWrongLiteralContent(final String literal) {
    try {
      EdmSimpleTypeKind.parseUriLiteral(literal);
      fail("Expected UriParserException not thrown");
    } catch (EdmLiteralException e) {
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
    parseWrongLiteralContent("9876543210");
    parseWrongLiteralContent("-9876543210");
    parseWrongLiteralContent("12345678901234567890L");
    parseWrongLiteralContent("12345678901234567890D");
    parseWrongLiteralContent("1234567890F");
    parseWrongLiteralContent("guid'a'");
    parseWrongLiteralContent("datetime'1'");
    parseWrongLiteralContent("datetimeoffset'2'");
    parseWrongLiteralContent("time'3'");
  }

  private void parseIncompatibleLiteralContent(final String literal, final EdmSimpleTypeKind typeKind) throws EdmLiteralException {
    final EdmLiteral uriLiteral = EdmSimpleTypeKind.parseUriLiteral(literal);
    assertFalse(typeKind.getEdmSimpleTypeInstance().isCompatible(uriLiteral.getType()));
  }

  @Test
  public void parseIncompatibleLiteral() throws Exception {
    parseIncompatibleLiteralContent("1D", EdmSimpleTypeKind.Binary);
    parseIncompatibleLiteralContent("'0'", EdmSimpleTypeKind.Boolean);
    parseIncompatibleLiteralContent("'1'", EdmSimpleTypeKind.Boolean);
    parseIncompatibleLiteralContent("2", EdmSimpleTypeKind.Boolean);
    parseIncompatibleLiteralContent("-1", EdmSimpleTypeKind.Byte);
    parseIncompatibleLiteralContent("-129", EdmSimpleTypeKind.Byte);
    parseIncompatibleLiteralContent("time'PT11H12M13S'", EdmSimpleTypeKind.DateTime);
    parseIncompatibleLiteralContent("time'PT11H12M13S'", EdmSimpleTypeKind.DateTimeOffset);
    parseIncompatibleLiteralContent("'1'", EdmSimpleTypeKind.Decimal);
    parseIncompatibleLiteralContent("1M", EdmSimpleTypeKind.Double);
    parseIncompatibleLiteralContent("1", EdmSimpleTypeKind.Guid);
    parseIncompatibleLiteralContent("32768", EdmSimpleTypeKind.Int16);
    parseIncompatibleLiteralContent("1L", EdmSimpleTypeKind.Int32);
    parseIncompatibleLiteralContent("1M", EdmSimpleTypeKind.Int64);
    parseIncompatibleLiteralContent("128", EdmSimpleTypeKind.SByte);
    parseIncompatibleLiteralContent("1D", EdmSimpleTypeKind.Single);
    parseIncompatibleLiteralContent("1", EdmSimpleTypeKind.String);
    parseIncompatibleLiteralContent("datetime'2012-10-10T11:12:13'", EdmSimpleTypeKind.Time);
  }
}
