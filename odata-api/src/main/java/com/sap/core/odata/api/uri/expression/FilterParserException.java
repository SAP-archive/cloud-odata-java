package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * @author d039346
 *
 */
public class FilterParserException extends ODataBadRequestException
{
  private static final long serialVersionUID = 1L;

  /*TODO add error texts to resource file*/

  //VON ABAP "An exception occurred"
 
  public static final MessageReference COMMON_ERROR = createMessageReference(FilterParserException.class, "COMMON");

  //NEW Error during tokenizing
  public static final MessageReference ERROR_IN_TOKENIZER = createMessageReference(FilterParserException.class, "ERROR_IN_TOKENIZER");
  
  //VON ABAP CX_DS_EXPR_SYNTAX_ERROR-TOKEN_INVALID "Invalid token detected at position &POSITION&"
  public static final MessageReference INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING = createMessageReference(FilterParserException.class, "INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING");

  //NORTHWIND Expression expected at position 25.
  public static final MessageReference EXPRESSION_EXPECTED_AT_POS = createMessageReference(FilterParserException.class, "EXPRESSION_EXPECTED_AT_POS");//TODO

  public static final MessageReference INVALID_TOKEN = createMessageReference(FilterParserException.class, "INVALID_TOKEN");
  
  private static final MessageReference METHOD_TO_FEW_PARAMETERS = createMessageReference(ODataMessageException.class, "TODO");//TODO

  private static final MessageReference METHOD_TO_MANY_PARAMETERS = createMessageReference(ODataMessageException.class, "TODO");//TODO

  /*instance attributes*/
  private CommonExpression filterTree;

  /*--Constructors--*/
  public FilterParserException() {
    super(COMMON_ERROR);
  }

  public FilterParserException(MessageReference messageReference) {
    super(messageReference);
  }

  public FilterParserException(MessageReference messageReference, Throwable cause) {
    super(messageReference);
    this.initCause(cause);
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
  public FilterParserException setFilterTree(CommonExpression filterTree)
  {
    this.filterTree = filterTree;
    return this;
  }
  
  public FilterParserException setCause(Throwable tokenizerException) {
    this.initCause(tokenizerException);
    return this;
  }


  public static FilterParserException ErrorInTokenizer(String token, int position)
  {
    MessageReference msgRef = FilterParserException.ERROR_IN_TOKENIZER.addContent(Integer.toString(position), token);
    return new FilterParserException(msgRef);
    
  }
      


  public static FilterParserException NewToFewParameters(MethodExpression methodExpression) {
    return new FilterParserException(METHOD_TO_FEW_PARAMETERS);
  }

  public static FilterParserException NewToManyParameters(MethodExpression methodExpression) {
    return new FilterParserException(METHOD_TO_MANY_PARAMETERS);
  }


}
