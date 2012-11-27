package com.sap.core.odata.api.uri.expression;


/**
 * Contains an enum for all supported unary operators of this expression parser
 * implementation. 
 */
public enum UnaryOperator
{
  MINUS("-"),
  NOT("not");
  
  
  private String stringRespresentation;
  
  /**
   * Constructor  
   * @param stringRespresentation
   *   Just used to have a nice {@link #toString()} return value
   */
  UnaryOperator(String stringRespresentation)
  {
    this.stringRespresentation = stringRespresentation;
  }
  
  @Override
  public String toString()
  {
    return stringRespresentation;
  }
}