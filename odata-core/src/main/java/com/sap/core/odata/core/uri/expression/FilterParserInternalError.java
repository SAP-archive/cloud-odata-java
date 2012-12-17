package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.CommonExpression;

public class FilterParserInternalError extends ODataMessageException {

  static final long serialVersionUID = 77L;
  public static final MessageReference ERROR_PARSING_METHOD = createMessageReference(FilterParserInternalError.class, "ERROR_PARSING_METHOD");
  public static final MessageReference ERROR_PARSING_PARENTHESIS = createMessageReference(FilterParserInternalError.class, "ERROR_PARSING_PARENTHESIS");
  public static final MessageReference ERROR_ACCESSING_EDM = createMessageReference(FilterParserInternalError.class, "ERROR_ACCESSING_EDM");

  CommonExpression parenthesisExpression = null;

  public FilterParserInternalError(MessageReference messageReference)
  {
    super(messageReference);
  }

  public FilterParserInternalError(MessageReference messageReference, TokenizerExpectError cause)
  {
    super(messageReference, cause);
  }

  public FilterParserInternalError(MessageReference messageReference, EdmException cause)
  {
    super(messageReference, cause);
  }

  public FilterParserInternalError setExpression(CommonExpression parenthesisExpression) {

    return this;
  }

  public static FilterParserInternalError createERROR_PARSING_METHOD(TokenizerExpectError cause)
  {
    return new FilterParserInternalError(ERROR_PARSING_METHOD, cause);
  }

  public static FilterParserInternalError createERROR_PARSING_PARENTHESIS(TokenizerExpectError cause)
  {
    return new FilterParserInternalError(ERROR_PARSING_PARENTHESIS, cause);
  }

  public static FilterParserInternalError createERROR_ACCESSING_EDM(EdmException cause)
  {
    return new FilterParserInternalError(ERROR_ACCESSING_EDM, cause);
  }

}
