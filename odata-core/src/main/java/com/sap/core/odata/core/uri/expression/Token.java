package com.sap.core.odata.core.uri.expression;
/*1*/

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.UriLiteral;

public class Token {
  
  
  private TokenKind kind;
  private int position;
  private EdmType edmType;
  private String uriLiteral;
  private UriLiteral javaLiteral;
  
  public Token(TokenKind kind, int position,  String uriLiteral, UriLiteral javaLiteral)
  {
    this.kind = kind;
    this.position = position;
    this.uriLiteral = uriLiteral;
    this.javaLiteral = javaLiteral;
  }
  
  public Token(TokenKind kind, int position,  String uriLiteral )
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
    return edmType;
  }

  public String getUriLiteral() 
  {
    return uriLiteral;
  }

  public UriLiteral getJavaLiteral() 
  {
    return javaLiteral;
  }
}
