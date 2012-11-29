package com.sap.core.odata.core.uri.expression;

/*1*/

/**
 * The token kind is used to categorize a single {@link Token}.
 * The Expression parser ({@link FilterParserImpl}) uses this information 
 * to build the expression tree.
 */
public enum TokenKind {
  /**
   * Indicates that the token is a whitespace character
   */
  WHITESPACE,

  /**
   * Indicates that the token is a '(' character
   */
  OPENPAREN,

  /**
   * Indicates that the token is a ')' character
   */
  CLOSEPAREN,

  /**
   * Indicates that the token is a ',' character
   */
  COMMA,

  /**
   * Indicates that the token is a typed literal. That may be a 
   * Edm.String like 'TEST'
   * Edm.Double like '1.1D' 
   * or any other Edm.Simple Type
   */
  SIMPLE_TYPE,

  /**
   * Indicates that the token is a single symbol. That may be a
   * '-', '=', '/', '?', '.' or a '*' character  
   */
  SYMBOL,

  /**
   * Indicates that the token is a set of alphanumeric characters starting
   * with a letter
   */
  LITERAL,
  TYPED_LITERAL_TODO_CHECK

}
