/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.fail;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class TestTokenizer
{

  @Test
  public void tokenizeWhiteSpaces() throws Exception
  {
    //space
    getTTW(" ")
        .at(0)
        .aKind(TokenKind.WHITESPACE)
        .aUriLiteral(" ")
        .aPosition(0);

    getTTW("   ")
        .at(0)
        .aKind(TokenKind.WHITESPACE)
        .aUriLiteral("   ")
        .aPosition(0);

    getTTW("A   ")
        .at(1)
        .aKind(TokenKind.WHITESPACE)
        .aUriLiteral("   ")
        .aPosition(1);

    getTTW("   B")
        .at(0)
        .aKind(TokenKind.WHITESPACE)
        .aUriLiteral("   ")
        .aPosition(0);

    getTTW("A   B")
        .at(1)
        .aKind(TokenKind.WHITESPACE)
        .aUriLiteral("   ")
        .aPosition(1)
        .at(2)
        .aKind(TokenKind.LITERAL)
        .aUriLiteral("B")
        .aPosition(4);

    getTTW("A   B   C")
        .at(3)
        .aKind(TokenKind.WHITESPACE)
        .aUriLiteral("   ")
        .aPosition(5);
  }

  @Test
  public void tokenizeSymbols() throws Exception
  {

    //parentheses
    getTT("(").aKind(TokenKind.OPENPAREN).aUriLiteral("(");
    getTT("abc(")
        .at(1)
        .aKind(TokenKind.OPENPAREN).aUriLiteral("(")
        .aPosition(3);

    getTT(")").aKind(TokenKind.CLOSEPAREN).aUriLiteral(")");
    getTT("abc)")
        .at(1)
        .aKind(TokenKind.CLOSEPAREN).aUriLiteral(")")
        .aPosition(3);

    //symbol
    getTT(",").aKind(TokenKind.COMMA).aUriLiteral(",");
    getTT("abc,")
        .at(1)
        .aKind(TokenKind.COMMA).aUriLiteral(",")
        .aPosition(3);

    //minus
    getTT("-").aKind(TokenKind.SYMBOL).aUriLiteral("-");
    getTT("abc -")
        .at(1)
        .aKind(TokenKind.SYMBOL).aUriLiteral("-")
        .aPosition(4);

    //minus after literal belongs to literal
    getTT("abc-")
        .at(0)
        .aKind(TokenKind.LITERAL).aUriLiteral("abc-")
        .aPosition(0);

  }

  public static String HexToBase64(final String hex)
  {
    String base64 = "";
    byte bArr[];
    try {
      bArr = Hex.decodeHex(hex.toCharArray());
      base64 = Base64.encodeBase64String(bArr);
    } catch (DecoderException e)
    {
      fail("Error in Unittest preparation ( HEX->base64");
    }
    return base64;
  }

  @Test
  public void tokenizeTypes() throws Exception
  {
    getTT("a").aKind(TokenKind.LITERAL).aUriLiteral("a").aPosition(0);
    getTT("abc a").at(1).aKind(TokenKind.LITERAL).aUriLiteral("a").aPosition(4);

    //string
    getTT("'a'").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("'a'").aPosition(0);
    getTT("abc 'a'").at(1).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("'a'").aPosition(4);

    //"prefixed type
    getTT("X'00'").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("X'00'").aPosition(0);
    getTT("abc X'00'").at(1).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("X'00'").aPosition(4);

    //simple types
    getTT("null").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("null").aPosition(0);
    getTT("abc null").at(1).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("null").aPosition(4);

    getTT("128").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("128").aPosition(0);
    getTT("abc 128").at(1).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("128").aPosition(4);

    //do special types
    getTT("datetime'2011-01-12T00:00:00'").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("datetime'2011-01-12T00:00:00'").aPosition(0);
    getTT("abc datetime'2011-01-12T00:00:00'").at(1).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("datetime'2011-01-12T00:00:00'").aPosition(4);
  }

  @Test
  public void tokenizeOperators() throws Exception
  {
    getTT("a eq b").at(1).aKind(TokenKind.LITERAL).aUriLiteral("eq");
    getTT("a eqotto b").at(1).aKind(TokenKind.LITERAL).aUriLiteral("eqotto");
  }

  @Test
  public void testExceptions() throws Exception
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter='a
    //-->Unterminated string literal at position 2 in ''a'.
    getTT("'a")
        .aExMsgText("Unterminated string literal at position 1 in \"'a\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=X'g'
    //-->Unrecognized 'Edm.Binary' literal 'X'g'' in '0'.
    getTT("X'g'")
        .aExMsgText("Type detection error for string like token 'X'g'' at position '1'.");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=\
    //-->Syntax error '\' at position 0.
    getTT("\\")
        .aExMsgText("Unknown character '\\' at position '0' detected in \"\\\".");
  }

  @Test
  public void tokenizeFunction() throws Exception
  {
    getTT("substringof('10')")
        .at(0).aKind(TokenKind.LITERAL).aUriLiteral("substringof")
        .at(2).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("'10'");

    getTT("substringof  (  '10'  )  ")
        .at(0).aKind(TokenKind.LITERAL).aUriLiteral("substringof")
        .at(2).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("'10'");
  }

  @Test
  public void testEx1111ceptions() throws Exception
  {
    getTT("a 1")
        .at(0).aKind(TokenKind.LITERAL).aUriLiteral("a")
        .at(1).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("1");

    getTT("a eq b").at(0).aKind(TokenKind.LITERAL).aUriLiteral("a")
        .at(1).aKind(TokenKind.LITERAL).aUriLiteral("eq")
        .at(2).aKind(TokenKind.LITERAL).aUriLiteral("b");

    getTT("start_date eq datetime'2011-01-12T00:00:00' and end_date eq datetime'2011-12-31T00:00:00'")
        .at(2).aUriLiteral("datetime'2011-01-12T00:00:00'");

    getTT("'a%b'").at(0).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("'a%b'");

  }

  /**
   * Create TokenTool ( and Token list) without respecting whitespaces
   * @param expression Expression to be tokenized
   */
  public TokenTool getTT(final String expression)
  {
    return new TokenTool(expression, false);
  }

  /**
   * Create TokenTool ( and Token list) without respecting whitespaces
   * @param expression Expression to be tokenized
   */
  public TokenTool getTTW(final String expression)
  {
    return new TokenTool(expression, true);
  }
}
