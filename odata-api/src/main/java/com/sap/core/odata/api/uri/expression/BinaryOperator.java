package com.sap.core.odata.api.uri.expression;
/*1*/
 
/**
 * Contains enumerations for all supported binary operators of the ODATA expression parser
*/
public enum BinaryOperator
{
  AND,
  OR,
  EQ,
  NE,
  LT,
  LE,
  GT,
  GE,
  ADD,
  SUB,
  MUL,
  PROPERTY_ACCESS,
  DIV,
  MODULO,
  /*TODO the operators above are already supported
   * are there unsupported operators which should be mentioned here
   */
}