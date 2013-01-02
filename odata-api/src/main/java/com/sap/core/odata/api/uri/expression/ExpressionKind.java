package com.sap.core.odata.api.uri.expression;
/*1*/
/**
 * Enumeration describing all possible node types inside an expression tree
 * @author SAP AG
 */
public enum ExpressionKind {
  /**
   * Used to mark the root node of a filter expression tree
   * @see FilterExpression  
   */
  FILTER,
  /**
   * Literals like "1.1d" or "'This is a string'"
   * @see LiteralExpression 
   */
  LITERAL,
  
  /**
   * Unary operators like "not" and "-"
   * @see UnaryExpression
   */
  UNARY,
  
  /**
   * Binary operators like "eq" and "or" 
   * @see BinaryExpression
   */
  BINARY,
  
  /**
   * Method operators like "substringof" and "concat" 
   * @see MethodExpression
   */
  METHOD,
  
  /**
   * Member access operator like "/" in "adress/street"
   * @see MemberExpression 
   */
  MEMBER,
  
  /**
   * Property like "age" 
   * @see PropertyExpression
   */
  PROPERTY,
  
  /**
   * Order expression like "age desc" 
   * @see PropertyExpression
   */
  ORDER,
  
  /**
   * Orderby expression like "age desc, name asc" 
   * @see PropertyExpression
   */
  ORDERBY;
}
