package com.sap.core.odata.api.uri.expression;


import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * @author d039346
 *
 */
public class ExceptionParseExpression extends ODataBadRequestException 
{
  private static final long serialVersionUID = 1L;

  /*TODO add error texts to resource file*/
  
  //VON ABAP "An exception occurred"
  public static final MessageReference COMMON = createMessageReference(ODataMessageException.class, "COMMON");
  
  //VON ABAP CX_DS_EXPR_SYNTAX_ERROR-FILTER_ERROR "Error while parsing $filter expression"
  public static final MessageReference ERR_IN_TOKENIZER = createMessageReference(ODataMessageException.class, "ERR_IN_TOKENIZER");
  
  //VON ABAP CX_DS_EXPR_SYNTAX_ERROR-TOKEN_INVALID "Invalid token detected at position &POSITION&"
  public static final MessageReference INVALID_TRAILING_TOKEN_DETECTED  = createMessageReference(ODataMessageException.class, "INVALID_TRAILING_TOKEN");
  
  //NEW Error during tokenizing
  public static final MessageReference ERROR_IN_TOKENIZER = createMessageReference(ODataMessageException.class, "ERROR_IN_TOKENIZER");;
  
  //ABAP Expected token &TOKEN& not found
  public static final MessageReference UNEXPECTED_TOKEN = createMessageReference(ODataMessageException.class, "UNEXPECTED_TOKEN");

  //NORTHWIND Expression expected at position 25.
  public static final MessageReference EXPRESSION_EXPECTED_AT_POS = null;

  public static MessageReference TO_FEW_PARAMETERS;

  public static MessageReference TO_MANY_PARAMETERS;

  public static MessageReference INVALID_TOKEN;



  
  public ExceptionParseExpression previous;
  private CommonExpression filterTree;


  public Exception getPrevious() {
    return previous;
  }


  public ExceptionParseExpression() {
    super(COMMON);
  }

  public ExceptionParseExpression(MessageReference messageReference) {
    super(messageReference);
  }

  
  /**
   * @param message
   * @param cause
   *   Cause of this exception, should i.e. an instance of ExceptionTokenizer 
   */
  public ExceptionParseExpression(MessageReference message, Throwable cause) {
    super(message);
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
  public ExceptionParseExpression setFilterTree(CommonExpression filterTree)
  {
    this.filterTree = filterTree;
    return this;
  }


  public static ExceptionParseExpression NewToFewParameters(MethodExpression methodExpression) {
    return new ExceptionParseExpression(TO_FEW_PARAMETERS);
  }


  public static ExceptionParseExpression NewToManyParameters(MethodExpression methodExpression) {
    return new ExceptionParseExpression(TO_MANY_PARAMETERS);
  }


  public ExceptionParseExpression setCause(Throwable tokenizerException) {
    this.initCause(tokenizerException);
    return this;
  }


  public ExceptionParseExpression setMessage(MessageReference errorInTokenizer) {
    //super.messageReference = errorInTokenizer;
    return null;
  }


}

