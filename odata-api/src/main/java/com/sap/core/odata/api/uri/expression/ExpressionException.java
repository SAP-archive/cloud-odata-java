package com.sap.core.odata.api.uri.expression;


import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * @author d039346
 *
 */
public class ExpressionException extends ODataMessageException 
{
  private static final long serialVersionUID = 1L;

  /*TODO add error texts to resource file*/
  
  //VON ABAP "An exception occurred"
  public static final MessageReference COMMON = createMessageReference(ODataMessageException.class, "COMMON");
  
  //VON ABAP CX_DS_EXPR_SYNTAX_ERROR-FILTER_ERROR "Error while parsing $filter expression"
  public static final MessageReference ERR_IN_TOKENIZER = createMessageReference(ODataMessageException.class, "ERR_IN_TOKENIZER");
  
  //VON ABAP CX_DS_EXPR_SYNTAX_ERROR-TOKEN_INVALID "Invalid token detected at position &POSITION&"
  public static final MessageReference INVALID_TRAILING_TOKEN  = createMessageReference(ODataMessageException.class, "INVALID_TRAILING_TOKEN");


  
  public ExpressionException previous;
  private CommonExpression filterTree;


  public Exception getPrevious() {
    return previous;
  }


  public ExpressionException() {
    super(COMMON);
  }

  public ExpressionException(MessageReference messageReference) {
    super(messageReference);
  }

  
  public ExpressionException(MessageReference invalidTrailingToken, Object object) {
    super(invalidTrailingToken);
  }


  /**
   * Get erroneous filter for debug information
   * @return Erroneous filter tree 
   */
  public CommonExpression getFilterTree() {
    return filterTree;
  }
  
  /**
   * See erroneous filter tree for debug information
   * @param filterTree
   */
  public void setFilterTree(CommonExpression filterTree)
  {
    this.filterTree = filterTree;
  }


}

