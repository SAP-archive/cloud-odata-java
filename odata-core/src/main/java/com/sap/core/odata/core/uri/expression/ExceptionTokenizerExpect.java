package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.ExceptionParseExpression;

/**
 * This exception is thrown if a token should be read
 * from the top of the {@link TokenList} which does not match an 
 * expected token. The cause for using this exception <b>MUST</b> indicate an internal error 
 * in the {@link Tokenizer} or inside the {@link FilterParserImpl}.
 * <br><br>
 * <b>This exception in not in the public API</b>, but may be added as cause for
 * the {@link ExceptionExpressionInternalError} exception. 
 * @author SAP AG
  */
public class ExceptionTokenizerExpect extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final int ParseStringToken = 1;
  
//Invalid token detected at position &POSITION&
  static final MessageReference INVALID_TOKEN_AT = createMessageReference(ExceptionTokenizerExpect.class, "INVALID_TOKEN_AT");

  private String token;
  private Exception previous;
  private int position;

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

  public ExceptionTokenizerExpect(MessageReference messageReference)
  {
    super( messageReference);
    //this.textID = object;
    this.token = token;
    this.previous = previous;
  }

  public static Object unexpectedToken(int currentToken, String string, Token actual) {
    // TODO Auto-generated method stub
    return null;
  }
}
