package com.sap.core.odata.api.uri.expression;
/*1*/
 
/**
 * Contains enumerations for supported binary operators of the ODATA expression parser 
 * for ODATA version 2.0 (with some restrictions)
*/
public enum BinaryOperator
{
  AND("and"),
  OR("or"),
  EQ("eq"),
  NE("ne"),
  LT("lt"),
  LE("le"),
  GT("gt"),
  GE("ge"),
  ADD("add"),
  SUB("sub"),
  MUL("mul"),
  PROPERTY_ACCESS("/"),
  DIV("div"),
  MODULO("mod");
  /*TODO the operators above are already supported
   * are there unsupported operators which should be mentioned here
   */
  
  private String syntax;
  private String stringRespresentation;
  
  
  BinaryOperator(String syntax)
  {
    this.syntax = syntax;
    this.stringRespresentation = syntax;
  }
  
  BinaryOperator( String syntax, String stringRespresentation)
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