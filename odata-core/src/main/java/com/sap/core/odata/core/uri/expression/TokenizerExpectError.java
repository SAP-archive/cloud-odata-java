package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * This exception is thrown if a token should be read
 * from the top of the {@link TokenList} which does not match an 
 * expected token. The cause for using this exception <b>MUST</b> indicate an internal error 
 * in the {@link Tokenizer} or inside the {@link FilterParserImpl}.
 * <br><br>
 * <b>This exception in not in the public API</b>, but may be added as cause for
 * the {@link ExpressionParserInternalError} exception. 
 * @author SAP AG
  */
public class TokenizerExpectError extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final int parseStringpoken = 1;

  //Invalid token detected at position &POSITION&
  public static final MessageReference NO_TOKEN_AVAILABLE = createMessageReference(TokenizerExpectError.class, "NO_TOKEN_AVAILABLE");
  public static final MessageReference INVALID_TOKEN_AT = createMessageReference(TokenizerExpectError.class, "INVALID_TOKEN_AT");
  public static final MessageReference INVALID_TOKENKIND_AT = createMessageReference(TokenizerExpectError.class, "INVALID_TOKENKIND_AT");

  private String token;
  private Exception previous;
  private int position;

  public String getToken() 
  {
    return token;
  }

  public void setToken(String token) 
  {
    this.token = token;
  }

  public Exception getPrevious() 
  {
    return previous;
  }

  public void setPrevious(Exception previous) 
  {
    this.previous = previous;
  }

  public int getPosition() 
  {
    return position;
  }

  public void setPosition(int position) 
  {
    this.position = position;
  }

  public TokenizerExpectError(MessageReference messageReference)
  {
    super(messageReference);
  }

  public static Object unexpectedToken(int currentToken, String string, Token actual) {
    // TODO Auto-generated method stub
    return null;
  }

  public static TokenizerExpectError createINVALID_TOKEN_AT(String expectedToken, Token actualToken)
  {
    MessageReference msgRef = TokenizerExpectError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedToken);
    msgRef.addContent(actualToken.getUriLiteral());
    msgRef.addContent(actualToken.getPosition());

    return new TokenizerExpectError(msgRef);
  }

  public static TokenizerExpectError createINVALID_TOKENKIND_AT(TokenKind expectedTokenKind, Token actualToken)
  {
    MessageReference msgRef = TokenizerExpectError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedTokenKind.toString());
    msgRef.addContent(actualToken.getKind().toString());
    msgRef.addContent(actualToken.getUriLiteral());
    msgRef.addContent(actualToken.getPosition());

    return new TokenizerExpectError(msgRef);
  }
  
  public static TokenizerExpectError createNO_TOKEN_AVAILABLE(String expectedToken)
  {
    MessageReference msgRef = TokenizerExpectError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedToken);

    return new TokenizerExpectError(msgRef);
  }

}
