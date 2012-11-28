package com.sap.core.odata.api.uri.expression;
/*1*/
/**
 * Enum describing all possible node types inside an expression tree
 * @author SAP AG
 *
 */
public enum ExpressionKind {
  /**
   * Literals like "1.1d" or "'This is a string'" 
   */
  LITERAL,
  
  /**
   * Unary operators like "not" and "-" 
   * @see {@link UnaryExpression}
   */
  UNARY,
  
  /**
   * Binary operators like "eq" and "or" 
   * @see {@link BinaryExpression}
   */
  BINARY,
  
  /**
   * Method operators like "substringof" and "concat" 
   * @see {@link MethodExpression}
   */
  METHOD,
  
  /**
   * Member access operator like "/" in "adress/street"
   * @see {@link MemberExpression} 
   */
  MEMBER,
  
  /**
   * Property like "age" 
   * @see {link {@link PropertyExpression}
   */
  PROPERTY;
}
