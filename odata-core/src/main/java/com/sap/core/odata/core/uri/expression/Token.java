package com.sap.core.odata.core.uri.expression;

/*1*/

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.EdmLiteral;

public class Token {

  private TokenKind kind;
  private int position;
  private String uriLiteral;
  private EdmLiteral javaLiteral;

  public Token(TokenKind kind, int position, String uriLiteral, EdmLiteral javaLiteral)
  {
    this.kind = kind;
    this.position = position;
    this.uriLiteral = uriLiteral;
    this.javaLiteral = javaLiteral;
  }

  public Token(TokenKind kind, int position, String uriLiteral)
  {
    this.kind = kind;
    this.position = position;
    this.uriLiteral = uriLiteral;
    this.javaLiteral = null;
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
