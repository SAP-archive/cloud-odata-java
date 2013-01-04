package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.sap.core.odata.core.uri.expression.TokenizerException;
import com.sap.core.odata.core.uri.expression.Token;
import com.sap.core.odata.core.uri.expression.TokenKind;
import com.sap.core.odata.core.uri.expression.TokenList;
import com.sap.core.odata.core.uri.expression.Tokenizer;

public class TestTokenizer {

  protected class TokenTool
  {
    protected Token token;
    protected TokenList tokens = null;

    /**
     * Set the token to be check to the token at position <code>index</code>
     * 
     * @param index Index of the token to be checked
     * @return Returns <code>this</code>
     * @throws AssertionError
     */
    public TokenTool at(int index)
    {
      token = tokens.elementAt(index);
      return this;
    }

    /**
     * Checks that the Type of the token matches the <code>kind</code>
     *      
     * @param kind Kind to be compared with the token type
     * @return Returns <code>this</code>
     * @throws AssertionError 
     */
    public TokenTool aKind(TokenKind kind)
    {
      assertEquals(token.getKind(), kind);
      return this;
    }

    /**
     * Checks that the EDM Type of the token matches the <code>edmType</code>
     * 
     * @param edmType EDM Type to be compared with the token type
     * @return Returns <code>this</code>
     * @throws AssertionError
     */
    public TokenTool aEdmType(int edmType)
    {
      assertEquals(token.getEdmType(), edmType);
      return this;
    }

    /**
     * Checks that the Value of the token matches the <code>stringValue</code>
     * 
     * @param stringValue Value to be compared with the token value 
     * @return Returns <code>this</code>
     * @throws AssertionError
     */
    public TokenTool aUriLiteral(String stringValue)
    {
      assertEquals(token.getUriLiteral(), stringValue);
      return this;
    }

    TokenTool(TokenList tokens2)
    {
      this.tokens = tokens2;
      this.at(0);
    }
  }

  /**
   * Create TokenTool ( and Token list) without respecting whitespaces
   * @param expression Expression to be tokenized
   */
  public TokenTool GetTT(String expression)
  {
    Tokenizer tokenizer = new Tokenizer();
    try {
      TokenList tokens = tokenizer.tokenize(expression);//please 
      return new TokenTool(tokens);
      //    } catch (TokenizerRTException e) {
      //      fail("Error in tokenize" + e.getLocalizedMessage());
    } catch (TokenizerException e) {

      fail("Error in tokenize" + e.getLocalizedMessage());
    }
    return null;
  }

  /**
   * Create TokenTool ( and Token list) without respecting whitespaces
   * @param expression Expression to be tokenized
   */
  public TokenTool GetTTW(String expression)
  {
    Tokenizer tokenizer = new Tokenizer().setFlagWhiteSpace(true);
    try {
      TokenList tokens = tokenizer.tokenize(expression);
      return new TokenTool(tokens);
      //    } catch (TokenizerRuntimeException e) {
      //      fail("Error in tokenize" + e.getLocalizedMessage());
    } catch (TokenizerException e) {
      fail("Error in tokenize" + e.getLocalizedMessage());
    }
    return null;
  }

  @Test
  public void tokenizeSymbols() throws Exception
  {/*
    //space
    GetTTW(" ").aKind(TokenKind.WHITESPACE).aUriLiteral(" ");

    //parentheses
    GetTT("(").aKind(TokenKind.OPENPAREN).aUriLiteral("(");
    GetTT(")").aKind(TokenKind.CLOSEPAREN).aUriLiteral(")");

    //symbol
    GetTT(",").aKind(TokenKind.SYMBOL).aUriLiteral(",");

    //minus
    GetTT("-").aKind(TokenKind.SYMBOL).aUriLiteral("-");*/
  }

  public static String HexToBase64(String hex)
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
  {/*
    //return;
    GetTT("a").aKind(TokenKind.LITERAL).aUriLiteral("a");

    //string
    GetTT("'a'").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("a");

    //"prefixed type
    GetTT("X'00'").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral(HexToBase64("00"));

    //simple types
    GetTT("null").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("null");
    GetTT("128").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("128");

    //do special types
    GetTT("datetime'2011-01-12T00:00:00'").aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("2011-01-12T00:00:00");*/
  }

  @Test
  public void tokenizeOperators() throws Exception
  {/*
    //return;
    GetTT("a eq b").at(1).aKind(TokenKind.LITERAL).aUriLiteral("eq");
    GetTT("a eqotto b").at(1).aKind(TokenKind.LITERAL).aUriLiteral("eqotto");*/
  }

  //@Test
  public void tokenizeOther1() throws Exception
  {
    //return;
    /*
    METHOD test_erroneous_tokens.
    \"check for string literal without trailing '
    expect_bad_request_exception(  "'a"
                                  iv_textid = /iwcor/cx_ds_expr_syntax_error=>token_invalid ).

    *    \"check for illegal binary value
    *    expect_bad_request_exception(  "X'g'"
    *                                  iv_textid = /IWCOR/cx_DS_expr_syntax_error=>token_invalid ).

    \"check for illegal special character
    expect_bad_request_exception(  "\"
                                  iv_textid = /iwcor/cx_ds_expr_syntax_error=>token_invalid ).
    ENDMETHOD.                    \"test_erroneous_tokens*/
  }

  @Test
  public void tokenizeFunction() throws Exception
  {/*
    //return;
    GetTT("substringof('10')")
        .at(0).aKind(TokenKind.LITERAL).aUriLiteral("substringof")
        .at(2).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("10");

    GetTT("substringof  (  '10'  )  ")
        .at(0).aKind(TokenKind.LITERAL).aUriLiteral("substringof")
        .at(2).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("10");*/

  }

  //@Test
  public void tokenizeOther() throws Exception
  {/*
    //return;
    //other literal
    GetTT("a 1")
        .at(0).aKind(TokenKind.LITERAL).aUriLiteral("a")
        .at(1).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("1");

    GetTT("a eq b").at(0).aKind(TokenKind.LITERAL).aUriLiteral("a")
        .at(1).aKind(TokenKind.LITERAL).aUriLiteral("eq")
        .at(2).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("b");

    GetTT("start_date eq datetime'2011-01-12T00:00:00' and end_date eq datetime'2011-12-31T00:00:00'")
        .at(2).aUriLiteral("datetime'2011-01-12T00:00:00'");

    GetTT("'a%b'").at(0).aKind(TokenKind.SIMPLE_TYPE).aUriLiteral("'a%b'");*/

  }
}
