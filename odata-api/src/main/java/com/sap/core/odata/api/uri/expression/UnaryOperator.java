package com.sap.core.odata.api.uri.expression;


/**
 * Contains an enum for all supported unary operators of this expression parser
 * implementation. 
 */
public enum UnaryOperator
{
  MINUS("-"),
  NOT("not");
  
  
  private String syntax;
  private String stringRespresentation;
  
  
  UnaryOperator(String syntax)
  {
    this.syntax = syntax;
    this.stringRespresentation = syntax;
  }
  
  UnaryOperator(String syntax, String stringRespresentation)
  {
    this.syntax = syntax;
    this.stringRespresentation = stringRespresentation;
  }
  
  @Override
  public String toString()
  {
    return stringRespresentation;
  }
  
  public String toSyntax()
  {
    return syntax;
  }
}