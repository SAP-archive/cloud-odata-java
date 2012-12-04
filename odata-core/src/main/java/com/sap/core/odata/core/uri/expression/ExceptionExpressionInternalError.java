package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.CommonExpression;

public class ExceptionExpressionInternalError extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static MessageReference ERROR_PARSING_METHOD;
  public static MessageReference ERROR_PARSING_PARENTHESIS;

  /*public ExceptionExpressionInternalError(MessageReference messageReference) {
    super(messageReference);
  }*/
  
  /**
   * This constructor should be used if the cause for the internal expression error is 
   * 
   * @param messageReference
   * @param cause
   */
  public ExceptionExpressionInternalError(MessageReference messageReference, ExceptionTokenizerExpect cause) 
  {
    super(messageReference);
    this.initCause(cause);
  }

  public void setExpression(CommonExpression parenthesisExpression) {
    // TODO Auto-generated method stub
    
  }  

}
