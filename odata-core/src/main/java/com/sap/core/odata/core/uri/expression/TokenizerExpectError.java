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

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * This exception is thrown if a token should be read
 * from the top of the {@link TokenList} which does not match an 
 * expected token. The cause for using this exception <b>MUST</b> indicate an internal error 
 * in the {@link Tokenizer} or inside the {@link FilterParserImpl}.
 * <br><br>
 * <b>This exception in not in the public API</b>, but may be added as cause for
 * the {@link ExpressionParserInternalError} exception. 
 * @author SAP AG
  */
public class TokenizerExpectError extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final int parseStringpoken = 1;

  //Invalid token detected at position &POSITION&
  public static final MessageReference NO_TOKEN_AVAILABLE = createMessageReference(TokenizerExpectError.class, "NO_TOKEN_AVAILABLE");
  public static final MessageReference INVALID_TOKEN_AT = createMessageReference(TokenizerExpectError.class, "INVALID_TOKEN_AT");
  public static final MessageReference INVALID_TOKENKIND_AT = createMessageReference(TokenizerExpectError.class, "INVALID_TOKENKIND_AT");

  private String token;
  private Exception previous;
  private int position;

  public String getToken()
  {
    return token;
  }

  public void setToken(final String token)
  {
    this.token = token;
  }

  public Exception getPrevious()
  {
    return previous;
  }

  public void setPrevious(final Exception previous)
  {
    this.previous = previous;
  }

  public int getPosition()
  {
    return position;
  }

  public void setPosition(final int position)
  {
    this.position = position;
  }

  public TokenizerExpectError(final MessageReference messageReference)
  {
    super(messageReference);
  }

  public static TokenizerExpectError createINVALID_TOKEN_AT(final String expectedToken, final Token actualToken)
  {
    MessageReference msgRef = TokenizerExpectError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedToken);
    msgRef.addContent(actualToken.getUriLiteral());
    msgRef.addContent(actualToken.getPosition());

    return new TokenizerExpectError(msgRef);
  }

  public static TokenizerExpectError createINVALID_TOKENKIND_AT(final TokenKind expectedTokenKind, final Token actualToken)
  {
    MessageReference msgRef = TokenizerExpectError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedTokenKind.toString());
    msgRef.addContent(actualToken.getKind().toString());
    msgRef.addContent(actualToken.getUriLiteral());
    msgRef.addContent(actualToken.getPosition());

    return new TokenizerExpectError(msgRef);
  }

  public static TokenizerExpectError createNO_TOKEN_AVAILABLE(final String expectedToken)
  {
    MessageReference msgRef = TokenizerExpectError.INVALID_TOKEN_AT.create();

    msgRef.addContent(expectedToken);

    return new TokenizerExpectError(msgRef);
  }

}
