package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.FilterParserException;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;

/**
 * This class is used to create exceptions of type FilterParserException.
 * Because this class lies inside com.sap.core.odata.core it is possible to define better/more detailed 
 * input parameters for inserting into the exception text.<br>
 * The exception {@link FilterParserException} does not know the com.sap.core.odata.core content
 * 
 * @author SAP AG
 */
public class FilterParserExceptionImpl extends FilterParserException
{
  private static final long serialVersionUID = 77L;

  static public FilterParserException createCOMMON()
  {
    return new FilterParserException(ODataBadRequestException.COMMON);
  }

  static public FilterParserException createERROR_IN_TOKENIZER(TokenizerException exceptionTokenizer)
  {
    Token token = exceptionTokenizer.getToken();
    MessageReference msgRef = FilterParserException.ERROR_IN_TOKENIZER.create();

    msgRef.addContent(token.getUriLiteral());
    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef, exceptionTokenizer);
  }

  static public FilterParserException createINVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING(Token token, String filterExpression)
  {
    MessageReference msgRef = FilterParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING.create();

    msgRef.addContent(token.getUriLiteral());
    msgRef.addContent(Integer.toString(token.getPosition()));
    msgRef.addContent(filterExpression);

    return new FilterParserException(msgRef);
  }

  static public FilterParserException createEXPRESSION_EXPECTED_AFTER_POS(Token token)
  {
    MessageReference msgRef = FilterParserException.EXPRESSION_EXPECTED_AFTER_POS.create();

    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef);
  }

  static public FilterParserException createEXPRESSION_EXPECTED_AT_POS(Token token)
  {
    MessageReference msgRef = FilterParserException.EXPRESSION_EXPECTED_AT_POS.create();

    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef);
  }

  static public FilterParserException createCOMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS(Token token)
  {
    MessageReference msgRef = FilterParserException.COMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS.create();

    msgRef.addContent(Integer.toString(token.getPosition() + token.getUriLiteral().length()));

    return new FilterParserException(msgRef);
  }

  static public FilterParserException createINVALID_TOKEN(Token token)
  {
    MessageReference msgRef = FilterParserException.INVALID_TOKEN.create();

    msgRef.addContent(token.getUriLiteral());
    msgRef.addContent(Integer.toString(token.getPosition()));

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createMETHOD_WRONG_ARG_COUNT(String expression, MethodExpressionImpl methodExpression, Token token)
  {
    MessageReference msgRef = null;
    int minParam = methodExpression.getMethodInfo().getMinParameter();
    int maxParam = methodExpression.getMethodInfo().getMaxParameter();

    if ((minParam == -1) && (maxParam == -1))
    {
      int i = 1;
      //no exception thrown in this case
    }
    else if ((minParam != -1) && (maxParam == -1))
    {
      //Tested with TestParserExceptions.TestPMreadParameters CASE 7-1
      msgRef = FilterParserException.METHOD_WRONG_ARG_X_OR_MORE.create();
      msgRef.addContent(methodExpression.getMethod().toUriLiteral());
      msgRef.addContent(token.getPosition());
      msgRef.addContent(expression);
      msgRef.addContent(minParam);
    }
    else if ((minParam == -1) && (maxParam != -1))
    {
      //Tested with TestParserExceptions.TestPMreadParameters CASE 8-2
      msgRef = FilterParserException.METHOD_WRONG_ARG_X_OR_LESS.create();
      msgRef.addContent(methodExpression.getMethod().toUriLiteral());
      msgRef.addContent(token.getPosition());
      msgRef.addContent(expression);
      msgRef.addContent(maxParam);
    }
    else if ((minParam != -1) && (maxParam != -1))
    {
      if (minParam == maxParam)
      {
        //Tested with TestParserExceptions.TestPMreadParameters CASE 11-1
        msgRef = FilterParserException.METHOD_WRONG_ARG_EXACT.create();
        msgRef.addContent(methodExpression.getMethod().toUriLiteral());
        msgRef.addContent(token.getPosition());
        msgRef.addContent(expression);
        msgRef.addContent(minParam);
      }
      else
      {
        //Tested with TestParserExceptions.TestPMreadParameters CASE 10-1
        msgRef = FilterParserException.METHOD_WRONG_ARG_BETWEEN.create();
        msgRef.addContent(methodExpression.getMethod().toUriLiteral());
        msgRef.addContent(token.getPosition());
        msgRef.addContent(expression);
        msgRef.addContent(minParam);
        msgRef.addContent(maxParam);
      }
    }

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createMETHOD_TO_MANY_PARAMETERS(MethodExpression methodExpression)
  {
    MessageReference msgRef = FilterParserException.METHOD_TO_MANY_PARAMETERS.create();

    msgRef.addContent(methodExpression.getMethod().toUriLiteral());

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createLEFT_SIDE_NOT_STRUCTURAL_TYPE()
  {
    MessageReference msgRef = FilterParserException.LEFT_SIDE_NOT_STRUCTURAL_TYPE.create();

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createPROPERTY_NAME_NOT_FOUND_IN_TYPE(EdmStructuralType parentType, PropertyExpression property) throws FilterParserInternalError
  {
    MessageReference msgRef = FilterParserException.PROPERTY_NAME_NOT_FOUND_IN_TYPE.create();

    try {
      msgRef.addContent(property.getPropertyName());
      msgRef.addContent(parentType.getNamespace() + "." + parentType.getName());
    } catch (EdmException e) {
      throw FilterParserInternalError.createERROR_ACCESSING_EDM(e);
    }

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createTOKEN_UNDETERMINATED_STRING(int position, String expression) {
    MessageReference msgRef = FilterParserException.TOKEN_UNDETERMINATED_STRING.create();

    msgRef.addContent(position);
    msgRef.addContent(expression);

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createINVALID_TYPES_FOR_BINARY_OPERATOR(int position, String expression, BinaryOperator op, EdmType left, EdmType right) {
    MessageReference msgRef = FilterParserException.INVALID_TYPES_FOR_BINARY_OPERATOR.create();

    msgRef.addContent(op.toUriLiteral());

    try {
      msgRef.addContent(left.getNamespace() + "." + left.getName());
    } catch (EdmException e) {
      msgRef.addContent("");
    }
    try {
      msgRef.addContent(right.getNamespace() + "." + right.getName());
    } catch (EdmException e) {
      msgRef.addContent("");
    }
    msgRef.addContent(position);
    msgRef.addContent(expression);

    return new FilterParserException(msgRef);
  }

  public static FilterParserException createMISSING_CLOSING_PHARENTHESIS(int position, String expression, TokenizerExpectError e) {
    MessageReference msgRef = FilterParserException.MISSING_CLOSING_PHARENTHESIS.create();

    msgRef.addContent(position);
    msgRef.addContent(expression);

    return new FilterParserException(msgRef, e);
  }

}
