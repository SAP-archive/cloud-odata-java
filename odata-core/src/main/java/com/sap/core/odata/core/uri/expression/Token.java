package com.sap.core.odata.core.uri.expression;

/*1*/

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmType;

public class Token {

  private TokenKind kind;
  private int position;
  private String uriLiteral;
  private EdmLiteral javaLiteral;

  public Token(final TokenKind kind, final int position, final String uriLiteral, final EdmLiteral javaLiteral)
  {
    this.kind = kind;
    this.position = position;
    this.uriLiteral = uriLiteral;
    this.javaLiteral = javaLiteral;
  }

  public Token(final TokenKind kind, final int position, final String uriLiteral)
  {
    this.kind = kind;
    this.position = position;
    this.uriLiteral = uriLiteral;
    javaLiteral = null;
  }

  public TokenKind getKind()
  {
    return kind;
  }

  public int getPosition()
  {
    return position;
  }

  public EdmType getEdmType()
  {
    if (javaLiteral == null)
      return null;
    return javaLiteral.getType();
  }

  public String getUriLiteral()
  {
    return uriLiteral;
  }

  public EdmLiteral getJavaLiteral()
  {
    return javaLiteral;
  }
}
