package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * This exception is thrown if there is an error during tokenizing.<br>
 * <b>This exception in not in the public API</b>, but may be added as cause for
 * the {@link com.sap.core.odata.api.uri.expression.FilterParserException} exception.  
 * @author SAP AG
 */
public class ExceptionTokenizer extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final int ParseStringToken = 1;

  public static MessageReference PARSESTRINGTOKEN;
  
  private int textID;
  private String tokenString;
  private Token token;
  private Exception previous;
  private int position;

  public Token getToken() {
    return token;
  }

  //TODO move to Token structured  
  public void setToken(String token) {
    this.tokenString = token;
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

  public ExceptionTokenizer(MessageReference pARSESTRINGTOKEN2)
  {
    super(pARSESTRINGTOKEN2);

  }
}
