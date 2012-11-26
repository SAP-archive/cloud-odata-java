package com.sap.core.odata.api.uri.expression;


/**
 * Contains an enum for all supported binary operators of this expression parser
 * implementation. The String given to the constructor is the String defined in 
 * the OData expression format which is used to determine the operator. 
 */
public enum MethodOperator 
{
  ENDSWITH,
  INDEXOF,
  STARTSWITH,
  TOLOWER,
  TOUPPER,
  TRIM,
  SUBSTRING,
  SUBSTRINGOF,
  CONCAT,
  LENGTH,
  YEAR,
  MONTH,
  DAY,
  HOUR,
  MINUTE,
  SECOND,
  ROUND,
  FLOOR,
  CEILING;
}