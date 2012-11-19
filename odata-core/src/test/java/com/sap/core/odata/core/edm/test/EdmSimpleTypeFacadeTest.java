package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;
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
import com.sap.core.odata.core.edm.EdmSingle;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.core.edm.EdmTime;
import com.sap.core.odata.core.edm.Uint7;

public class EdmSimpleTypeFacadeTest {

  public EdmSimpleType parse(String literal) throws UriParserException {
    UriLiteral uriLiteral = EdmSimpleTypeFacade.parseUriLiteral(literal);
    return uriLiteral.getType();
  }

  public void compare(EdmSimpleTypeKind simpleType) {
    EdmSimpleType bin1 = EdmSimpleTypeFacade.getInstance(simpleType);
    assertNotNull(bin1);

    EdmSimpleType bin2 = EdmSimpleTypeFacade.getInstance(simpleType);
    assertNotNull(bin2);

    assertTrue(bin1.equals(bin2));
  }

  @Test
  public void parseUriLiteralBinary() throws UriParserException {
    EdmSimpleType binary = parse("X'Fa12aAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmBinary);

    binary = parse("binary'FA12AAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmBinary);

  }

  @Test
  public void parseUriLiteralBoolean() throws UriParserException {
    EdmSimpleType bool = parse("true");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmBoolean);

    bool = parse("false");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmBoolean);
  }

  @Test
  public void parseUriLiteralBit() throws UriParserException {
    EdmSimpleType bit = parse("1");
    assertNotNull(bit);
    assertTrue(bit instanceof Bit);

    bit = parse("0");
    assertNotNull(bit);
    assertTrue(bit instanceof Bit);
  }

  @Test
  public void parseUriLiteralByte() throws UriParserException {
    EdmSimpleType byt = parse("255");
    assertNotNull(byt);
    assertTrue(byt instanceof EdmByte);
  }

  @Test
  public void parseUriLiteralUint7() throws UriParserException {
    EdmSimpleType uInt7 = parse("123");
    assertNotNull(uInt7);
    assertTrue(uInt7 instanceof Uint7);
  }

  @Test
  public void parseUriLiteralDateTime() throws UriParserException {
    EdmSimpleType dt = parse("datetime'2009-12-26T21%3A23%3A38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmDateTime);

    dt = parse("datetime'2009-12-26T21%3A23%3A38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmDateTime);
  }

  @Test
  public void parseUriLiteralDateTimeOffset() throws UriParserException {
    EdmSimpleType dto = parse("datetimeoffset'2009-12-26T21%3A23%3A38Z'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmDateTimeOffset);

    dto = parse("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmDateTimeOffset);
  }

  @Test
  public void parseUriLiteralDecimal() throws UriParserException {
    EdmSimpleType dec = parse("4.5m");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmDecimal);

    dec = parse("4.5M");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmDecimal);
  }

  @Test
  public void parseUriLiteralDouble() throws UriParserException {
    EdmSimpleType doub = parse("4.5d");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmDouble);

    doub = parse("4.5D");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmDouble);
  }

  @Test
  public void parseUriLiteralGuid() throws UriParserException {
    EdmSimpleType guid = parse("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'");
    assertNotNull(guid);
    assertTrue(guid instanceof EdmGuid);
  }

  @Test
  public void parseUriLiteralInt16() throws UriParserException {
    EdmSimpleType in = parse("-32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt16);

    in = parse("3276");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt16);
  }

  @Test
  public void parseUriLiteralInt32() throws UriParserException {
    EdmSimpleType in = parse("-327687");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt32);

    in = parse("32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt32);
  }

  @Test
  public void parseUriLiteralInt64() throws UriParserException {
    EdmSimpleType in = parse("64L");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt64);

    in = parse("64l");
    assertNotNull(in);
    assertTrue(in instanceof EdmInt64);
  }

  @Test
  public void parseUriLiteralNull() throws UriParserException {

  }

  @Test
  public void parseUriLiteralSByte() throws UriParserException {
    EdmSimpleType sb = parse("-123");
    assertNotNull(sb);
    assertTrue(sb instanceof EdmSByte);
  }

  @Test
  public void parseUriLiteralSingle() throws UriParserException {
    EdmSimpleType sing = parse("4.5f");
    assertNotNull(sing);
    assertTrue(sing instanceof EdmSingle);
  }

  @Test
  public void parseUriLiteralString() throws UriParserException {
    EdmSimpleType str = parse("'abc'");
    assertNotNull(str);
    assertTrue(str instanceof EdmString);
  }

  @Test
  public void parseUriLiteralTime() throws UriParserException {
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
   * @throws UriParserException 
   * @throws EdmException
   */
  private void parseLiteral(final String literal, final EdmSimpleType type, final String expectedLiteral) throws UriParserException, EdmException {
    final UriLiteral uriLiteral = EdmSimpleTypeFacade.parseUriLiteral(literal);

    assertTrue(type.isCompatible(uriLiteral.getType()));
    assertEquals(expectedLiteral, uriLiteral.getLiteral());
  }

  @Test
  public void parseDecimal() throws Exception {
    parseLiteral("4.5m", EdmSimpleTypeFacade.decimalInstance(), "4.5");
    parseLiteral("4.5M", EdmSimpleTypeFacade.decimalInstance(), "4.5");
    parseLiteral("1", EdmSimpleTypeFacade.decimalInstance(), "1");
    parseLiteral("255", EdmSimpleTypeFacade.decimalInstance(), "255");
    parseLiteral("-32768", EdmSimpleTypeFacade.decimalInstance(), "-32768");
    parseLiteral("32768", EdmSimpleTypeFacade.decimalInstance(), "32768");
    parseLiteral("3000000", EdmSimpleTypeFacade.decimalInstance(), "3000000");
    parseLiteral("4.5d", EdmSimpleTypeFacade.decimalInstance(), "4.5");
    parseLiteral("4.2E9F", EdmSimpleTypeFacade.decimalInstance(), "4.2E9");
    parseLiteral("1234567890", EdmSimpleTypeFacade.decimalInstance(), "1234567890");
  }

  @Test
  public void parseInt16() throws Exception {
    parseLiteral("16", EdmSimpleTypeFacade.int16Instance(), "16");
    parseLiteral("-16", EdmSimpleTypeFacade.int16Instance(), "-16");
    parseLiteral("255", EdmSimpleTypeFacade.int16Instance(), "255");
    parseLiteral("-32768", EdmSimpleTypeFacade.int16Instance(), "-32768");

  }

  @Test
  public void parseInt32() throws Exception {
    parseLiteral("32", EdmSimpleTypeFacade.int32Instance(), "32");
    parseLiteral("-127", EdmSimpleTypeFacade.int32Instance(), "-127");
    parseLiteral("255", EdmSimpleTypeFacade.int32Instance(), "255");
    parseLiteral("32767", EdmSimpleTypeFacade.int32Instance(), "32767");
    parseLiteral("-32769", EdmSimpleTypeFacade.int32Instance(), "-32769");
  }

  @Test
  public void parseInt64() throws Exception {
    parseLiteral("64", EdmSimpleTypeFacade.int64Instance(), "64");
    parseLiteral("255", EdmSimpleTypeFacade.int64Instance(), "255");
    parseLiteral("1000", EdmSimpleTypeFacade.int64Instance(), "1000");
    parseLiteral("100000", EdmSimpleTypeFacade.int64Instance(), "100000");
    parseLiteral("-64L", EdmSimpleTypeFacade.int64Instance(), "-64");
    parseLiteral("" + Long.MAX_VALUE + "L", EdmSimpleTypeFacade.int64Instance(), "" + Long.MAX_VALUE);
    parseLiteral("" + Long.MIN_VALUE + "l", EdmSimpleTypeFacade.int64Instance(), "" + Long.MIN_VALUE);
  }

  @Test
  public void parseString() throws Exception {
    parseLiteral("'abc'", EdmSimpleTypeFacade.stringInstance(), "abc");
    parseLiteral("'true'", EdmSimpleTypeFacade.stringInstance(), "true");
    parseLiteral("''", EdmSimpleTypeFacade.stringInstance(), "");
  }

  @Test
  public void parseSingle() throws Exception {
    parseLiteral("45", EdmSimpleTypeFacade.singleInstance(), "45");
    parseLiteral("255", EdmSimpleTypeFacade.singleInstance(), "255");
    parseLiteral("-32768", EdmSimpleTypeFacade.singleInstance(), "-32768");
    parseLiteral("32768", EdmSimpleTypeFacade.singleInstance(), "32768");
    parseLiteral("1L", EdmSimpleTypeFacade.singleInstance(), "1");
    parseLiteral("4.5f", EdmSimpleTypeFacade.singleInstance(), "4.5");
    parseLiteral("4.5F", EdmSimpleTypeFacade.singleInstance(), "4.5");
    parseLiteral("4.5e9f", EdmSimpleTypeFacade.singleInstance(), "4.5e9");
  }

  @Test
  public void parseDouble() throws Exception {
    parseLiteral("45", EdmSimpleTypeFacade.doubleInstance(), "45");
    parseLiteral("255", EdmSimpleTypeFacade.doubleInstance(), "255");
    parseLiteral("-32768", EdmSimpleTypeFacade.doubleInstance(), "-32768");
    parseLiteral("32768", EdmSimpleTypeFacade.doubleInstance(), "32768");
    parseLiteral("1l", EdmSimpleTypeFacade.doubleInstance(), "1");
    parseLiteral("4.5d", EdmSimpleTypeFacade.doubleInstance(), "4.5");
    parseLiteral("4.5D", EdmSimpleTypeFacade.doubleInstance(), "4.5");
    parseLiteral("4.5e21f", EdmSimpleTypeFacade.doubleInstance(), "4.5e21");
  }

  @Test
  public void parseByte() throws Exception {
    parseLiteral("255", EdmSimpleTypeFacade.byteInstance(), "255");
    parseLiteral("123", EdmSimpleTypeFacade.byteInstance(), "123");
  }

  @Test
  public void parseGuid() throws Exception {
    parseLiteral("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'", EdmSimpleTypeFacade.guidInstance(), "1225c695-cfb8-4ebb-aaaa-80da344efa6a");
  }

  @Test
  public void parseTime() throws Exception {
    parseLiteral("time'P120D'", EdmSimpleTypeFacade.timeInstance(), "P120D");
  }

  @Test
  public void parseDatetime() throws Exception {
    parseLiteral("datetime'2009-12-26T21:23:38'", EdmSimpleTypeFacade.dateTimeInstance(), "2009-12-26T21:23:38");
    parseLiteral("datetime'2009-12-26T21:23:38Z'", EdmSimpleTypeFacade.dateTimeInstance(), "2009-12-26T21:23:38Z");
  }

  @Test
  public void parseDatetimeOffset() throws Exception {
    parseLiteral("datetimeoffset'2009-12-26T21:23:38Z'", EdmSimpleTypeFacade.dateTimeOffsetInstance(), "2009-12-26T21:23:38Z");
    parseLiteral("datetimeoffset'2002-10-10T12:00:00-05:00'", EdmSimpleTypeFacade.dateTimeOffsetInstance(), "2002-10-10T12:00:00-05:00");
  }

  @Test
  public void parseBoolean() throws Exception {
    parseLiteral("true", EdmSimpleTypeFacade.booleanInstance(), "true");
    parseLiteral("false", EdmSimpleTypeFacade.booleanInstance(), "false");
    parseLiteral("1", EdmSimpleTypeFacade.booleanInstance(), "1");
    parseLiteral("0", EdmSimpleTypeFacade.booleanInstance(), "0");
  }

  @Test
  public void parseSByte() throws Exception {
    parseLiteral("-123", EdmSimpleTypeFacade.sByteInstance(), "-123");
    parseLiteral("12", EdmSimpleTypeFacade.sByteInstance(), "12");
  }

  @Test
  public void parseBinary() throws Exception {
    parseLiteral("X'Fa12aAA1'", EdmSimpleTypeFacade.binaryInstance(), "+hKqoQ==");
    parseLiteral("binary'FA12AAA1'", EdmSimpleTypeFacade.binaryInstance(), "+hKqoQ==");
  }

  private void parseWrongLiteralContent(final String literal) {
    try {
      EdmSimpleTypeFacade.parseUriLiteral(literal);
      fail("Expected UriParserException not thrown");
    } catch (UriParserException e) {
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

  private void parseIncompatibleLiteralContent(final String literal, final EdmSimpleType type) throws UriParserException {
    final UriLiteral uriLiteral = EdmSimpleTypeFacade.parseUriLiteral(literal);
    assertFalse(type.isCompatible(uriLiteral.getType()));
  }

  @Test
  public void parseIncompatibleLiteral() throws Exception {
    parseIncompatibleLiteralContent("1D", EdmSimpleTypeFacade.binaryInstance());
    parseIncompatibleLiteralContent("'0'", EdmSimpleTypeFacade.booleanInstance());
    parseIncompatibleLiteralContent("'1'", EdmSimpleTypeFacade.booleanInstance());
    parseIncompatibleLiteralContent("2", EdmSimpleTypeFacade.booleanInstance());
    parseIncompatibleLiteralContent("-1", EdmSimpleTypeFacade.byteInstance());
    parseIncompatibleLiteralContent("-129", EdmSimpleTypeFacade.byteInstance());
    parseIncompatibleLiteralContent("time'PT11H12M13S'", EdmSimpleTypeFacade.dateTimeInstance());
    parseIncompatibleLiteralContent("time'PT11H12M13S'", EdmSimpleTypeFacade.dateTimeOffsetInstance());
    parseIncompatibleLiteralContent("'1'", EdmSimpleTypeFacade.decimalInstance());
    parseIncompatibleLiteralContent("1M", EdmSimpleTypeFacade.doubleInstance());
    parseIncompatibleLiteralContent("1", EdmSimpleTypeFacade.guidInstance());
    parseIncompatibleLiteralContent("32768", EdmSimpleTypeFacade.int16Instance());
    parseIncompatibleLiteralContent("1L", EdmSimpleTypeFacade.int32Instance());
    parseIncompatibleLiteralContent("1M", EdmSimpleTypeFacade.int64Instance());
    parseIncompatibleLiteralContent("128", EdmSimpleTypeFacade.sByteInstance());
    parseIncompatibleLiteralContent("1D", EdmSimpleTypeFacade.singleInstance());
    parseIncompatibleLiteralContent("1", EdmSimpleTypeFacade.stringInstance());
    parseIncompatibleLiteralContent("datetime'2012-10-10T11:12:13'", EdmSimpleTypeFacade.timeInstance());
  }
}
