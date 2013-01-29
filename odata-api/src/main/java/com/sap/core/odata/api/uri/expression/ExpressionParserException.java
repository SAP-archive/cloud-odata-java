package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;

/**
 * Exception thrown while parsing a filter or orderby expression
 * @author SAP AG
 */
public class ExpressionParserException extends ODataBadRequestException
{
  private static final long serialVersionUID = 7702L;

  public static final MessageReference COMMON_ERROR = createMessageReference(ExpressionParserException.class, "COMMON");

  //token related exception texts
  public static final MessageReference ERROR_IN_TOKENIZER = createMessageReference(ExpressionParserException.class, "ERROR_IN_TOKENIZER");
  public static final MessageReference TOKEN_UNDETERMINATED_STRING = createMessageReference(ExpressionParserException.class, "TOKEN_UNDETERMINATED_STRING");
  public static final MessageReference INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING = createMessageReference(ExpressionParserException.class, "INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING");

  //parsing
  public static final MessageReference EXPRESSION_EXPECTED_AFTER_POS = createMessageReference(ExpressionParserException.class, "EXPRESSION_EXPECTED_AFTER_POS");
  public static final MessageReference COMMA_OR_END_EXPECTED_AT_POS = createMessageReference(ExpressionParserException.class, "COMMA_OR_END_EXPECTED_AT_POS");
  public static final MessageReference EXPRESSION_EXPECTED_AT_POS = createMessageReference(ExpressionParserException.class, "EXPRESSION_EXPECTED_AT_POS");
  public static final MessageReference MISSING_CLOSING_PHARENTHESIS = createMessageReference(ExpressionParserException.class, "MISSING_CLOSING_PHARENTHESIS");
  public static final MessageReference COMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS = createMessageReference(ExpressionParserException.class, "COMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS");
  public static final MessageReference INVALID_METHOD_CALL = createMessageReference(ExpressionParserException.class, "INVALID_METHOD_CALL");
  
  //validation exceptions texts - method
  public static final MessageReference METHOD_WRONG_ARG_EXACT = createMessageReference(ExpressionParserException.class, "METHOD_WRONG_ARG_EXACT");
  public static final MessageReference METHOD_WRONG_ARG_BETWEEN = createMessageReference(ExpressionParserException.class, "METHOD_WRONG_ARG_BETWEEN");
  public static final MessageReference METHOD_WRONG_ARG_X_OR_MORE = createMessageReference(ExpressionParserException.class, "METHOD_WRONG_ARG_X_OR_MORE");
  public static final MessageReference METHOD_WRONG_ARG_X_OR_LESS = createMessageReference(ExpressionParserException.class, "METHOD_WRONG_ARG_X_OR_LESS");

  //validation exceptions texts - member
  public static final MessageReference LEFT_SIDE_NOT_STRUCTURAL_TYPE = createMessageReference(ExpressionParserException.class, "LEFT_SIDE_NOT_STRUCTURAL_TYPE");
  public static final MessageReference LEFT_SIDE_NOT_A_PROPERTY = createMessageReference(ExpressionParserException.class, "LEFT_SIDE_NOT_A_PROPERTY");
  public static final MessageReference PROPERTY_NAME_NOT_FOUND_IN_TYPE = createMessageReference(ExpressionParserException.class, "PROPERTY_NAME_NOT_FOUND_IN_TYPE");

  //validation exceptions texts - binary
  public static final MessageReference INVALID_TYPES_FOR_BINARY_OPERATOR = createMessageReference(ExpressionParserException.class, "INVALID_TYPES_FOR_BINARY_OPERATOR");

  //orderby  
  public static final MessageReference INVALID_SORT_ORDER = createMessageReference(ExpressionParserException.class, "INVALID_SORT_ORDER");

  //instance attributes
  private CommonExpression filterTree;

  //Constructors
  public ExpressionParserException()
  {
    super(COMMON_ERROR);
  }

  /**
   * Create {@link ExpressionParserException} with given {@link MessageReference}.
   * 
   * @param messageReference
   *   references the message text (and additional values) of this {@link ExpressionParserException}
   */
  public ExpressionParserException(MessageReference messageReference)
  {
    super(messageReference);
  }

  /**
   * Create {@link ExpressionParserException} with given {@link MessageReference} and cause {@link Throwable} which caused
   * this {@link ExpressionParserException}.
   * 
   * @param messageReference
   *   References the message text (and additional values) of this {@link ExpressionParserException}
   * @param cause
   *   Exception which caused this {@link ExpressionParserException}
   */
  public ExpressionParserException(MessageReference messageReference, Throwable cause)
  {
    super(messageReference, cause);
  }

  /**
   * Get erroneous filter expression tree for debug information
   * @return 
   *   Erroneous filter tree 
   */
  public CommonExpression getFilterTree() {
    return filterTree;
  }

  /**
   * Set erroneous filter tree for debug information
   * @param filterTree 
   *   FilterTree to be set
   * @return 
   *   A self reference for method chaining"
   */
  public ExpressionParserException setFilterTree(CommonExpression filterTree)
  {
    this.filterTree = filterTree;
    return this;
  }

  /**
   * Set exception cause
   * @param cause 
   *   Exception that cause this exception 
   * @return 
   *   A self reference for method chaining"
   */
  public ExpressionParserException setCause(Throwable cause) 
  {
    initCause(cause);
    return this;
  }
}
