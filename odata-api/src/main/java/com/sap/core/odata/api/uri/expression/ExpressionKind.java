package com.sap.core.odata.api.uri.expression;
/*1*/
public enum ExpressionKind {
  /**
   * Unary operators like "not" and "-" 
   */
  UNARY,
  /**
   * Binary operators like "eq" and "or" 
   */
  BINARY,
  /**
   * Method operators like "substringof" and "concat" 
   */
  METHOD,
  /**
   * Member access operator like "/" in "adress/street" 
   */
  MEMBER,
  /**
   * Litaral like "1.1d" or "'This is a string'" 
   */
  LITERAL,
  /**
   * Property like "age" 
   */
  PROPERTY;
}
