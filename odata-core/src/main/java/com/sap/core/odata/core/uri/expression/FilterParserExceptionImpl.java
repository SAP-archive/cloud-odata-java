package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.uri.expression.FilterParserException;
import com.sap.core.odata.api.uri.expression.MethodExpression;

/**
 * This class is used to create exceptions of type FilterParserException.
 * Because this class lies inside com.sap.core.odata.core it is possible to define better/more detailed 
 * input parameters for inserting into the exception text.<br>
 * The exception {@link FilterParserException} does not know the com.sap.core.odata.core content
 * 
 * @author SAP AG
 */
public class FilterParserExceptionImpl extends FilterParserException {

  private static final long serialVersionUID = 77L;

  static public FilterParserException createCOMMON()
  {
    return new FilterParserException(FilterParserException.COMMON);
  }

  static public FilterParserException createERROR_IN_TOKENIZER(ExceptionTokenizer exceptionTokenizer)
  {
    Token token = exceptionTokenizer.getToken();
    MessageReference msgRef = FilterParserException.ERROR_IN_TOKENIZER.create();

    msgRef.addContent(token.getUriLiteral());
    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef);
  }

  static public FilterParserException createINVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING(Token token)
  {
    MessageReference msgRef = FilterParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING.create();

    msgRef.addContent(token.getUriLiteral());
    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef);
  }

  static public FilterParserException createEXPRESSION_EXPECTED_AT_POS(Token token)
  {
    MessageReference msgRef = FilterParserException.EXPRESSION_EXPECTED_AT_POS.create();

    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef);
  }

  static public FilterParserException createINVALID_TOKEN(Token token)
  {
    MessageReference msgRef = FilterParserException.INVALID_TOKEN.create();

    msgRef.addContent(token.getUriLiteral());
    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createMETHOD_TO_FEW_PARAMETERS(MethodExpression methodExpression)
  {
    MessageReference msgRef = FilterParserException.METHOD_TO_FEW_PARAMETERS.create();
    
    msgRef.addContent(methodExpression.getMethod().toSyntax());

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createMETHOD_TO_MANY_PARAMETERS(MethodExpression methodExpression) {
    MessageReference msgRef = FilterParserException.METHOD_TO_MANY_PARAMETERS.create();
    
    msgRef.addContent(methodExpression.getMethod().toSyntax());

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createLEFT_SIDE_NOT_STRUCTURAL_TYPE() {
    MessageReference msgRef = FilterParserException.LEFT_SIDE_NOT_STRUCTURAL_TYPE.create();

    return new FilterParserException(msgRef);
  }

}
