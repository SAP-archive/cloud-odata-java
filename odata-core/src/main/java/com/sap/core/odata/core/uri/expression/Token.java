package com.sap.core.odata.core.uri.expression;


import java.util.Vector;

import com.sap.core.odata.api.edm.EdmType;

public class Token {
  
  
  private TokenKind kind;
  private int position;
  private EdmType edmType;
  private String stringValue;
  
  //TODO change return type to List<M> interface
  public static <M> Vector<M> CreateTokenList()
  {
    return new Vector<M>();
  }

  public Token(TokenKind kind, int position, EdmType edmType, String stringValue)
  {
    this.kind = kind;
    this.position = position;
    this.edmType = edmType;
    this.stringValue = stringValue;
    
  }
  
  public TokenKind getKind()
  {
    return kind;
  }
  
  public int getPosition()
  {
    return position;
  }

  public Object getEdmType() {
    // TODO Auto-generated method stub
    return edmType;
  }

  public Object getStringValue() {
    // TODO Auto-generated method stub
    return stringValue;
  }

}
