/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.CommonExpression;

/**
 * Internal error in the expression parser.
 * @author SAP AG
 */
public class ExpressionParserInternalError extends ODataMessageException {

  static final long serialVersionUID = 77L;
  public static final MessageReference ERROR_PARSING_METHOD = createMessageReference(ExpressionParserInternalError.class, "ERROR_PARSING_METHOD");
  public static final MessageReference ERROR_PARSING_PARENTHESIS = createMessageReference(ExpressionParserInternalError.class, "ERROR_PARSING_PARENTHESIS");
  public static final MessageReference ERROR_ACCESSING_EDM = createMessageReference(ExpressionParserInternalError.class, "ERROR_ACCESSING_EDM");
  public static final MessageReference INVALID_TYPE_COUNT = createMessageReference(ExpressionParserInternalError.class, "INVALID_TYPE_COUNT");;
  public static final MessageReference INVALID_TOKEN_AT = createMessageReference(ExpressionParserInternalError.class, "INVALID_TOKEN_AT");
  public static final MessageReference INVALID_TOKENKIND_AT = createMessageReference(ExpressionParserInternalError.class, "INVALID_TOKENKIND_AT");

  CommonExpression parenthesisExpression = null;

  public ExpressionParserInternalError(final MessageReference messageReference)
  {
    super(messageReference);
  }

  public ExpressionParserInternalError(final MessageReference messageReference, final Throwable cause)
  {
    super(messageReference, cause);
  }

  public ExpressionParserInternalError(final MessageReference messageReference, final TokenizerExpectError cause)
  {
    super(messageReference, cause);
  }

  public ExpressionParserInternalError(final MessageReference messageReference, final EdmException cause)
  {
    super(messageReference, cause);
  }

  public ExpressionParserInternalError setExpression(final CommonExpression parenthesisExpression) {
    this.parenthesisExpression = parenthesisExpression;
    return this;
  }

  public static ExpressionParserInternalError createERROR_PARSING_METHOD(final TokenizerExpectError cause)
  {
    return new ExpressionParserInternalError(ERROR_PARSING_METHOD, cause);
  }

  public static ExpressionParserInternalError createERROR_PARSING_PARENTHESIS(final TokenizerExpectError cause)
  {
    return new ExpressionParserInternalError(ERROR_PARSING_PARENTHESIS, cause);
  }

  public static ExpressionParserInternalError createERROR_PARSING_PARENTHESIS(final CommonExpression parenthesisExpression, final TokenizerExpectError cause)
  {
    return new ExpressionParserInternalError(ERROR_PARSING_PARENTHESIS, cause).setExpression(parenthesisExpression);
  }

  public static ExpressionParserInternalError createERROR_ACCESSING_EDM(final EdmException cause)
  {
    return new ExpressionParserInternalError(ERROR_ACCESSING_EDM, cause);
  }

  public static ExpressionParserInternalError createCOMMON()
  {
    return new ExpressionParserInternalError(COMMON);
  }

  public static ExpressionParserInternalError createCOMMON(final Throwable e)
  {
    return new ExpressionParserInternalError(COMMON, e);
  }

  public static ExpressionParserInternalError createINVALID_TYPE_COUNT()
  {
    return new ExpressionParserInternalError(INVALID_TYPE_COUNT);
  }

  public static ExpressionParserInternalError createERROR_ACCESSING_EDM()
  {
    return new ExpressionParserInternalError(ERROR_ACCESSING_EDM);
  }

  public static ExpressionParserInternalError createINVALID_TOKEN_AT(final String expectedToken, final Token actualToken)
  {
    MessageReference msgRef = ExpressionParserInternalError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedToken);
    msgRef.addContent(actualToken.getUriLiteral());
    msgRef.addContent(actualToken.getPosition());

    return new ExpressionParserInternalError(msgRef);
  }

  public static ExpressionParserInternalError createINVALID_TOKENKIND_AT(final TokenKind expectedTokenKind, final Token actualToken)
  {
    MessageReference msgRef = ExpressionParserInternalError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedTokenKind.toString());
    msgRef.addContent(actualToken.getKind().toString());
    msgRef.addContent(actualToken.getUriLiteral());
    msgRef.addContent(actualToken.getPosition());

    return new ExpressionParserInternalError(msgRef);
  }

  public static ExpressionParserInternalError createNO_TOKEN_AVAILABLE(final String expectedToken)
  {
    MessageReference msgRef = ExpressionParserInternalError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedToken);

    return new ExpressionParserInternalError(msgRef);
  }

}
