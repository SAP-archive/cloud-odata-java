package com.sap.core.odata.api.uri.expression;

public enum SortOrder {
  asc("asc"),
  desc("desc");
  
  private String syntax;
  private String stringRespresentation;
  
  private SortOrder(String syntax)
  {
    this.syntax = syntax;
    this.stringRespresentation = syntax;
  }
  
  
  private SortOrder( String syntax, String stringRespresentation)
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
   * @return URI literal of the unary operator as used in the URL. 
   */
  public String toUriLiteral()
  {
    return syntax;
  }
}
