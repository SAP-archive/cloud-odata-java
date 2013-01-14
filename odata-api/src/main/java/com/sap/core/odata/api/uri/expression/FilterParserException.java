package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;

/**
 * @author SAP AG
 */
public class FilterParserException extends ODataBadRequestException
{
  private static final long serialVersionUID = 1L;



  //VON ABAP "An exception occurred"

  public static final MessageReference COMMON_ERROR = createMessageReference(FilterParserException.class, "COMMON");

  //NEW Error during tokenizing
  public static final MessageReference ERROR_IN_TOKENIZER = createMessageReference(FilterParserException.class, "ERROR_IN_TOKENIZER");

  //VON ABAP CX_DS_EXPR_SYNTAX_ERROR-TOKEN_INVALID "Invalid token detected at position &POSITION&"
  public static final MessageReference INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING = createMessageReference(FilterParserException.class, "INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING");

  
  public static final MessageReference EXPRESSION_EXPECTED_AFTER_POS = createMessageReference(FilterParserException.class, "EXPRESSION_EXPECTED_AFTER_POS");
  public static final MessageReference EXPRESSION_EXPECTED_AT_POS = createMessageReference(FilterParserException.class, "EXPRESSION_EXPECTED_AT_POS");
  public static final MessageReference COMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS = createMessageReference(FilterParserException.class, "COMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS");
  //public static final MessageReference INVALID_TOKEN = createMessageReference(FilterParserException.class, "INVALID_TOKEN");

  public static final MessageReference METHOD_WRONG_ARG_EXACT = createMessageReference(FilterParserException.class, "METHOD_WRONG_ARG_EXACT");
  public static final MessageReference METHOD_WRONG_ARG_BETWEEN = createMessageReference(FilterParserException.class, "METHOD_WRONG_ARG_BETWEEN");
  public static final MessageReference METHOD_WRONG_ARG_X_OR_MORE = createMessageReference(FilterParserException.class, "METHOD_WRONG_ARG_X_OR_MORE");
  public static final MessageReference METHOD_WRONG_ARG_X_OR_LESS = createMessageReference(FilterParserException.class, "METHOD_WRONG_ARG_X_OR_LESS");
  

  //public static final MessageReference METHOD_TO_MANY_PARAMETERS = createMessageReference(FilterParserException.class, "METHOD_TO_MANY_PARAMETERS");

  public static final MessageReference LEFT_SIDE_NOT_STRUCTURAL_TYPE = createMessageReference(FilterParserException.class, "LEFT_SIDE_NOT_STRUCTURAL_TYPE");
  public static final MessageReference LEFT_SIDE_NOT_A_PROPERTY = createMessageReference(FilterParserException.class, "LEFT_SIDE_NOT_A_PROPERTY");
  
  public static final MessageReference PROPERTY_NAME_NOT_FOUND_IN_TYPE = createMessageReference(FilterParserException.class, "PROPERTY_NAME_NOT_FOUND_IN_TYPE");

  public static final MessageReference TOKEN_UNDETERMINATED_STRING = createMessageReference(FilterParserException.class, "TOKEN_UNDETERMINATED_STRING");

  public static final MessageReference INVALID_TYPES_FOR_BINARY_OPERATOR = createMessageReference(FilterParserException.class, "INVALID_TYPES_FOR_BINARY_OPERATOR");
  
  public static final MessageReference MISSING_CLOSING_PHARENTHESIS= createMessageReference(FilterParserException.class, "MISSING_CLOSING_PHARENTHESIS");
  
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
    super(messageReference, cause);
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

}
