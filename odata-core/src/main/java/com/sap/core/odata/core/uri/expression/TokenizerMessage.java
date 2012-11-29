package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class TokenizerMessage extends ODataMessageException
{
  private static final long serialVersionUID = 1L;

  //Error in tokenizer
  public static final MessageReference COMMON = createMessageReference(TokenizerMessage.class, "COMMON");

  //Invalid token detected at position &POSITION&
  private static final MessageReference INVALID_TOKEN_AT = createMessageReference(TokenizerMessage.class, "INVALID_TOKEN_AT");

  public static final MessageReference PARSESTRINGTOKEN = createMessageReference(TokenizerMessage.class, "TODO"); //TODO

  private String token;
  private Exception previous;
  private int position;

  public TokenizerMessage(MessageReference messageReference) {
    super(messageReference);
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Exception getPrevious() {
    return previous;
  }

  public void setPrevious(Exception previous) {
    this.previous = previous;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public static TokenizerMessage unexpectedToken(int currentToken, String expected, Token actual) {
    // TODO Auto-generated method stub
    return null;
  }

}
