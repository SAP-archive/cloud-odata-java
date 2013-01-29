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

  public TokenizerException setToken(Token token)
  {
    this.token = token;
    return this;
  }

  public int getPosition()
  {
    return position;
  }

  public void setPosition(int position)
  {
    this.position = position;
  }

  public TokenizerException(MessageReference messageReference)
  {
    super(messageReference);
  }

  public TokenizerException(MessageReference messageReference, Throwable cause)
  {
    super(messageReference, cause);
  }

  static public TokenizerException createTYPEDECTECTION_FAILED_ON_STRING(EdmLiteralException ex, int position, String uriLiteral)
  {
    MessageReference msgRef = TokenizerException.TYPEDECTECTION_FAILED_ON_STRING.create();

    msgRef.addContent(uriLiteral);
    msgRef.addContent(position);
    Token token = new Token(TokenKind.UNKNOWN, position, uriLiteral);

    return new TokenizerException(msgRef).setToken(token);
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
  static public TokenizerException createUNKNOWN_CHARACTER(int position, String uriLiteral, String expression)
  {
    MessageReference msgRef = TokenizerException.UNKNOWN_CHARACTER.create();

    msgRef.addContent(uriLiteral);
    msgRef.addContent(position);
    msgRef.addContent(expression);
    Token token = new Token(TokenKind.UNKNOWN, position, uriLiteral);

    return new TokenizerException(msgRef).setToken(token);
  }

}
