package com.sap.core.odata.api.uri.expression;

/**
 * Enumerations for supported unary operators of the ODATA expression parser
 * for ODATA version 2.0 
 */
public enum UnaryOperator
{
  MINUS("-", "negation"),
  NOT("not");
  
  
  private String syntax;
  private String stringRespresentation;
  
  
  private UnaryOperator(String syntax)
  {
    this.syntax = syntax;
    this.stringRespresentation = syntax;
  }
  
  
  private UnaryOperator(String syntax, String stringRespresentation)
  {
    this.syntax = syntax;
    this.stringRespresentation = stringRespresentation;
  }
  
  
  /** 
   * @return Operators name for usage in in text
   */
  @Override
  public String toString()
  {
    return stringRespresentation;
  }
  
  
  /**
   * @return Syntax of the unary operator as used in the URL for the $filter parameter
   */
  public String toSyntax()
  {
    return syntax;
  }
}