package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.core.edm.simpletype.EdmBinary;
import com.sap.core.odata.core.edm.simpletype.EdmBit;
import com.sap.core.odata.core.edm.simpletype.EdmBoolean;
import com.sap.core.odata.core.edm.simpletype.EdmByte;
import com.sap.core.odata.core.edm.simpletype.EdmDateTime;
import com.sap.core.odata.core.edm.simpletype.EdmDateTimeOffset;
import com.sap.core.odata.core.edm.simpletype.EdmDecimal;
import com.sap.core.odata.core.edm.simpletype.EdmDouble;
import com.sap.core.odata.core.edm.simpletype.EdmGuid;
import com.sap.core.odata.core.edm.simpletype.EdmInt16;
import com.sap.core.odata.core.edm.simpletype.EdmInt32;
import com.sap.core.odata.core.edm.simpletype.EdmInt64;
import com.sap.core.odata.core.edm.simpletype.EdmSByte;
import com.sap.core.odata.core.edm.simpletype.EdmSingle;
import com.sap.core.odata.core.edm.simpletype.EdmString;
import com.sap.core.odata.core.edm.simpletype.EdmTime;
import com.sap.core.odata.core.edm.simpletype.EdmUint7;

public class EdmSimpleTypeFacadeTest {

  public EdmSimpleType parse(String literal) throws UriParserException {
    EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
    UriLiteral uriLiteral = facade.parseUriLiteral(literal);
    return uriLiteral.getType();
  }

  public void compare(EdmSimpleTypeKind simpleType) {
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
    assertTrue(bit instanceof EdmBit);

    bit = parse("0");
    assertNotNull(bit);
    assertTrue(bit instanceof EdmBit);
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
    assertTrue(uInt7 instanceof EdmUint7);
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

}
