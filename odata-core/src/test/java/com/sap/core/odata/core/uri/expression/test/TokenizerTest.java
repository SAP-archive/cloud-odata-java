package com.sap.core.odata.core.uri.expression.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Vector;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.sap.core.odata.core.uri.expression.Token;
import com.sap.core.odata.core.uri.expression.TokenKind;
import com.sap.core.odata.core.uri.expression.Tokenizer;
import com.sap.core.odata.core.uri.expression.TokenizerException;

public class TokenizerTest {

  protected class TokenTool
  {
    protected Token token;
    protected Vector<Token> tokens = null;

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
    public TokenTool aSValue(String stringValue)
    {
      assertEquals(token.getStringValue(), stringValue);
      return this;
    }

    TokenTool(Vector<Token> tokens)
    {
      this.tokens = tokens;
      this.at(0);
    }
  }

  /**
   * Create TokenTool ( and Token list) without respecting whitespaces
   * @param Expression to be tokenized
   * @return
   */
  public TokenTool GetTT(String expression)
  {
    Tokenizer tokenizer = new Tokenizer();
    try {
      Vector<Token> tokens = tokenizer.tokenize(expression);//please 
      return new TokenTool(tokens);
    } catch (TokenizerException e)
    {
      fail("Error in tokenize" + e.getLocalizedMessage());
      return null;
    }
  }

  /**
   * Create TokenTool ( and Token list) without respecting whitespaces
   * @param Expression to be tokenized
   * @return
   */
  public TokenTool GetTTW(String expression)
  {
    Tokenizer tokenizer = new Tokenizer().SetFlagWhiteSpace(true);
    try {
      Vector<Token> tokens = tokenizer.tokenize(expression);
      return new TokenTool(tokens);
    } catch (TokenizerException e) 
    {
      fail("Error in tokenize" + e.getLocalizedMessage());
      return null;
    }
  }

  @Test
  public void tokenizeSymbols() throws Exception
  {
    //space
    GetTTW(" ").aKind(TokenKind.WHITESPACE).aSValue(" ");

    //parentheses
    GetTT("(").aKind(TokenKind.OPENPAREN).aSValue("(");
    GetTT(")").aKind(TokenKind.CLOSEPAREN).aSValue(")");

    //symbol
    GetTT(",").aKind(TokenKind.SYMBOL).aSValue(",");

    //minus
    GetTT("-").aKind(TokenKind.SYMBOL).aSValue("-");
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
  {
    //return;
    GetTT("a").aKind(TokenKind.LITERAL).aSValue("a");

    //string
    GetTT("'a'").aKind(TokenKind.TYPED_LITERAL).aSValue("a");

    //"prefixed type
    GetTT("X'00'").aKind(TokenKind.TYPED_LITERAL).aSValue(HexToBase64("00"));

    //simple types
    GetTT("null").aKind(TokenKind.TYPED_LITERAL).aSValue("null");
    GetTT("128").aKind(TokenKind.TYPED_LITERAL).aSValue("128");

    //do special types
    GetTT("datetime'2011-01-12T00:00:00'").aKind(TokenKind.TYPED_LITERAL).aSValue("2011-01-12T00:00:00");
  }

  @Test
  public void tokenizeOperators() throws Exception
  {
    //return;
    GetTT("a eq b").at(1).aKind(TokenKind.LITERAL).aSValue("eq");
    GetTT("a eqotto b").at(1).aKind(TokenKind.LITERAL).aSValue("eqotto");
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
  {
    //return;
    GetTT("substringof('10')")
        .at(0).aKind(TokenKind.LITERAL).aSValue("substringof")
        .at(2).aKind(TokenKind.TYPED_LITERAL).aSValue("10");

    GetTT("substringof  (  '10'  )  ")
        .at(0).aKind(TokenKind.LITERAL).aSValue("substringof")
        .at(2).aKind(TokenKind.TYPED_LITERAL).aSValue("10");

  }

  //@Test
  public void tokenizeOther() throws Exception
  {
    //return;
    //other literal
    GetTT("a 1")
        .at(0).aKind(TokenKind.LITERAL).aSValue("a")
        .at(1).aKind(TokenKind.TYPED_LITERAL).aSValue("1");

    GetTT("a eq b").at(0).aKind(TokenKind.LITERAL).aSValue("a")
        .at(1).aKind(TokenKind.LITERAL).aSValue("eq")
        .at(2).aKind(TokenKind.TYPED_LITERAL).aSValue("b");

    GetTT("start_date eq datetime'2011-01-12T00:00:00' and end_date eq datetime'2011-12-31T00:00:00'")
        .at(2).aSValue("datetime'2011-01-12T00:00:00'");

    GetTT("'a%b'").at(0).aKind(TokenKind.TYPED_LITERAL).aSValue("'a%b'");

  }
}
