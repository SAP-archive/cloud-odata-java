package com.sap.core.odata.api.uri.expression;

/**
 * Enumeration describing all possible node types inside an expression tree
 * @author SAP AG
 */
public enum ExpressionKind
{
  /**
   * Used to mark the root node of a filter expression tree
   * @see FilterExpression  
   */
  FILTER,
  /**
   * Literal expressions like "1.1d" or "'This is a string'"
   * @see LiteralExpression 
   */
  LITERAL,

  /**
   * Unary operator expressions like "not" and "-"
   * @see UnaryExpression
   */
  UNARY,

  /**
   * Binary operator expressions like "eq" and "or" 
   * @see BinaryExpression
   */
  BINARY,

  /**
   * Method operator expressions like "substringof" and "concat" 
   * @see MethodExpression
   */
  METHOD,

  /**
   * Member access expressions like "/" in "adress/street"
   * @see MemberExpression 
   */
  MEMBER,

  /**
   * Property expressions like "age" 
   * @see PropertyExpression
   */
  PROPERTY,

  /**
   * Order expressions like "age desc" 
   * @see OrderExpression
   */
  ORDER,

  /**
   * Orderby expression like "age desc, name asc" 
   * @see OrderByExpression
   */
  ORDERBY;
}
