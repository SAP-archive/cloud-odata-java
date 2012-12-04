package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.FilterParserException;

/**
 * This exception is thrown if there is an error during tokenizing.<br>
 * <b>This exception in not in the public API</b>, but may be added as cause for
 * the {@link FilterParserException} exception.  
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

  public ExceptionTokenizer(MessageReference pARSESTRINGTOKEN2)
  {
    super(pARSESTRINGTOKEN2);

  }
}
