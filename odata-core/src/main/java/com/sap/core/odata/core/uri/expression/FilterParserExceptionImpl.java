package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;
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

    msgRef.addContent(methodExpression.getMethod().toUriLiteral());

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

  public static FilterParserException createTOKEN_UNDETERMINATED_STRING(int position, String iv_expression) {
    MessageReference msgRef = FilterParserException.TOKEN_UNDETERMINATED_STRING.create();

    msgRef.addContent(position);
    msgRef.addContent(iv_expression);

    return new FilterParserException(msgRef);
  }

}
