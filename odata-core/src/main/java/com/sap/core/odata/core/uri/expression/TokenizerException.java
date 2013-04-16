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

import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * This exception is thrown if there is an error during tokenizing.<br>
 * <b>This exception in not in the public API</b>, but may be added as cause for
 * the {@link com.sap.core.odata.api.uri.expression.ExpressionParserException} exception.  
 * @author SAP AG
 */
public class TokenizerException extends ODataMessageException
{
  private static final long serialVersionUID = 77L;

  public static final MessageReference TYPEDECTECTION_FAILED_ON_STRING = createMessageReference(TokenizerException.class, "TYPEDECTECTION_FAILED_ON_STRING");
  public static final MessageReference UNKNOWN_CHARACTER = createMessageReference(TokenizerException.class, "UNKNOWN_CHARACTER");

  private Token token;
  private int position;

  public Token getToken() {
    return token;
  }

  public TokenizerException setToken(final Token token)
  {
    this.token = token;
    return this;
  }

  public int getPosition()
  {
    return position;
  }

  public void setPosition(final int position)
  {
    this.position = position;
  }

  public TokenizerException(final MessageReference messageReference)
  {
    super(messageReference);
  }

  public TokenizerException(final MessageReference messageReference, final Throwable cause)
  {
    super(messageReference, cause);
  }

  static public TokenizerException createTYPEDECTECTION_FAILED_ON_STRING(final EdmLiteralException ex, final int position, final String uriLiteral)
  {
    MessageReference msgRef = TokenizerException.TYPEDECTECTION_FAILED_ON_STRING.create();

    msgRef.addContent(uriLiteral);
    msgRef.addContent(position);
    Token token = new Token(TokenKind.UNKNOWN, position, uriLiteral);

    return new TokenizerException(msgRef, ex).setToken(token);
  }

  /*
  static public TokenizerException createTYPEDECTECTION_FAILED_ON_EDMTYPE(EdmLiteralException ex, int position, String uriLiteral)
  {
    MessageReference msgRef = TokenizerException.TYPEDECTECTION_FAILED_ON_EDMTYPE.create();

    msgRef.addContent(uriLiteral);
    msgRef.addContent(position);
    Token token = new Token(TokenKind.UNKNOWN, position, uriLiteral);

    return new TokenizerException(msgRef).setToken(token);
  }
  */
  static public TokenizerException createUNKNOWN_CHARACTER(final int position, final String uriLiteral, final String expression)
  {
    MessageReference msgRef = TokenizerException.UNKNOWN_CHARACTER.create();

    msgRef.addContent(uriLiteral);
    msgRef.addContent(position);
    msgRef.addContent(expression);
    Token token = new Token(TokenKind.UNKNOWN, position, uriLiteral);

    return new TokenizerException(msgRef).setToken(token);
  }

}
