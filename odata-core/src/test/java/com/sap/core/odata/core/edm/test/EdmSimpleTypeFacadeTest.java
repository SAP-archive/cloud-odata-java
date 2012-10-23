package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade.EdmSimpleTypes;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeBinary;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeBit;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeBoolean;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeByte;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDateTime;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDateTimeOffset;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDecimal;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeDouble;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeGuid;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeInt16;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeInt32;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeInt64;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeSByte;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeSingle;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeString;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeTime;
import com.sap.core.odata.core.edm.simpletype.EdmSimpleTypeUint7;

public class EdmSimpleTypeFacadeTest {

  public EdmSimpleType parse(String literal) throws UriParserException {
    EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
    UriLiteral uriLiteral = facade.parseUriLiteral(literal);
    return uriLiteral.getType();
  }

  public void compare(EdmSimpleTypes simpleType) {
    EdmSimpleTypeFacade facade1 = new EdmSimpleTypeFacade();
    EdmSimpleType bin1 = facade1.getInstance(simpleType);
    assertNotNull(bin1);

    EdmSimpleTypeFacade facade2 = new EdmSimpleTypeFacade();
    EdmSimpleType bin2 = facade2.getInstance(simpleType);
    assertNotNull(bin2);

    assertTrue(bin1.equals(bin2));
  }

  @Test
  public void parseUriLiteralBinary() throws UriParserException {
    EdmSimpleType binary = parse("X'Fa12aAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmSimpleTypeBinary);

    binary = parse("binary'FA12AAA1'");
    assertNotNull(binary);
    assertTrue(binary instanceof EdmSimpleTypeBinary);

  }

  @Test
  public void parseUriLiteralBoolean() throws UriParserException {
    EdmSimpleType bool = parse("true");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmSimpleTypeBoolean);

    bool = parse("false");
    assertNotNull(bool);
    assertTrue(bool instanceof EdmSimpleTypeBoolean);
  }

  @Test
  public void parseUriLiteralBit() throws UriParserException {
    EdmSimpleType bit = parse("1");
    assertNotNull(bit);
    assertTrue(bit instanceof EdmSimpleTypeBit);

    bit = parse("0");
    assertNotNull(bit);
    assertTrue(bit instanceof EdmSimpleTypeBit);
  }

  @Test
  public void parseUriLiteralByte() throws UriParserException {
    EdmSimpleType byt = parse("255");
    assertNotNull(byt);
    assertTrue(byt instanceof EdmSimpleTypeByte);
  }

  @Test
  public void parseUriLiteralUint7() throws UriParserException {
    EdmSimpleType uInt7 = parse("123");
    assertNotNull(uInt7);
    assertTrue(uInt7 instanceof EdmSimpleTypeUint7);
  }

  @Test
  public void parseUriLiteralDateTime() throws UriParserException {
    EdmSimpleType dt = parse("datetime'2009-12-26T21%3A23%3A38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmSimpleTypeDateTime);

    dt = parse("datetime'2009-12-26T21%3A23%3A38'");
    assertNotNull(dt);
    assertTrue(dt instanceof EdmSimpleTypeDateTime);
  }

  @Test
  public void parseUriLiteralDateTimeOffset() throws UriParserException {
    EdmSimpleType dto = parse("datetimeoffset'2009-12-26T21%3A23%3A38Z'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmSimpleTypeDateTimeOffset);

    dto = parse("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'");
    assertNotNull(dto);
    assertTrue(dto instanceof EdmSimpleTypeDateTimeOffset);
  }

  @Test
  public void parseUriLiteralDecimal() throws UriParserException {
    EdmSimpleType dec = parse("4.5m");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmSimpleTypeDecimal);

    dec = parse("4.5M");
    assertNotNull(dec);
    assertTrue(dec instanceof EdmSimpleTypeDecimal);
  }

  @Test
  public void parseUriLiteralDouble() throws UriParserException {
    EdmSimpleType doub = parse("4.5d");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmSimpleTypeDouble);

    doub = parse("4.5D");
    assertNotNull(doub);
    assertTrue(doub instanceof EdmSimpleTypeDouble);
  }

  @Test
  public void parseUriLiteralGuid() throws UriParserException {
    EdmSimpleType guid = parse("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'");
    assertNotNull(guid);
    assertTrue(guid instanceof EdmSimpleTypeGuid);
  }

  @Test
  public void parseUriLiteralInt16() throws UriParserException {
    EdmSimpleType in = parse("-32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmSimpleTypeInt16);

    in = parse("3276");
    assertNotNull(in);
    assertTrue(in instanceof EdmSimpleTypeInt16);
  }

  @Test
  public void parseUriLiteralInt32() throws UriParserException {
    EdmSimpleType in = parse("-327687");
    assertNotNull(in);
    assertTrue(in instanceof EdmSimpleTypeInt32);

    in = parse("32768");
    assertNotNull(in);
    assertTrue(in instanceof EdmSimpleTypeInt32);
  }

  @Test
  public void parseUriLiteralInt64() throws UriParserException {
    EdmSimpleType in = parse("64L");
    assertNotNull(in);
    assertTrue(in instanceof EdmSimpleTypeInt64);

    in = parse("64l");
    assertNotNull(in);
    assertTrue(in instanceof EdmSimpleTypeInt64);
  }

  @Test
  public void parseUriLiteralNull() throws UriParserException {

  }

  @Test
  public void parseUriLiteralSByte() throws UriParserException {
    EdmSimpleType sb = parse("-123");
    assertNotNull(sb);
    assertTrue(sb instanceof EdmSimpleTypeSByte);
  }

  @Test
  public void parseUriLiteralSingle() throws UriParserException {
    EdmSimpleType sing = parse("4.5f");
    assertNotNull(sing);
    assertTrue(sing instanceof EdmSimpleTypeSingle);
  }

  @Test
  public void parseUriLiteralString() throws UriParserException {
    EdmSimpleType str = parse("'abc'");
    assertNotNull(str);
    assertTrue(str instanceof EdmSimpleTypeString);
  }

  @Test
  public void parseUriLiteralTime() throws UriParserException {
    EdmSimpleType time = parse("time'P120D'");
    assertNotNull(time);
    assertTrue(time instanceof EdmSimpleTypeTime);
  }

  @Test
  public void compareTypes() {
    compare(EdmSimpleTypes.BINARY);
    compare(EdmSimpleTypes.BOOLEAN);
    compare(EdmSimpleTypes.BYTE);
    compare(EdmSimpleTypes.SBYTE);
    compare(EdmSimpleTypes.DATETIME);
    compare(EdmSimpleTypes.DATETIMEOFFSET);
    compare(EdmSimpleTypes.DECIMAL);
    compare(EdmSimpleTypes.DOUBLE);
    compare(EdmSimpleTypes.GUID);
    compare(EdmSimpleTypes.INT16);
    compare(EdmSimpleTypes.INT32);
    compare(EdmSimpleTypes.INT64);
    compare(EdmSimpleTypes.NULL);
    compare(EdmSimpleTypes.SINGLE);
    compare(EdmSimpleTypes.TIME);
  }

}
