package com.sap.core.odata.core.uri.expression;
/*1*/

import com.sap.core.odata.api.edm.EdmType;

public class Token {
  
  
  private TokenKind kind;
  private int position;
  private EdmType edmType;
  private String uriLiteral;
  private String javaLiteral;
  

  public Token(TokenKind kind, int position, EdmType edmType, String uriLiteral, String javaLiteral)
  {
    this.kind = kind;
    this.position = position;
    this.edmType = edmType;
    this.uriLiteral = uriLiteral;
    this.javaLiteral = javaLiteral;
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

  public String getJavaLiteral() 
  {
    return javaLiteral;
  }
}
