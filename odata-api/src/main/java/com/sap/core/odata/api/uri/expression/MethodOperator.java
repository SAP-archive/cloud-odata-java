package com.sap.core.odata.api.uri.expression;

/**
 * Contains an enum for all supported binary operators of this expression parser
 * implementation. The String given to the constructor is the String defined in 
 * the OData expression format which is used to determine the operator. 
 */
public enum MethodOperator
{
  ENDSWITH("endswith"),
  INDEXOF("indexof"),
  STARTSWITH("startswith"),
  TOLOWER("tolower"),
  TOUPPER("toupper"),
  TRIM("trim"),
  SUBSTRING("substring"),
  SUBSTRINGOF("substringof"),
  CONCAT("concat"),
  LENGTH("length"),
  YEAR("year"),
  MONTH("month"),
  DAY("day"),
  HOUR("hour"),
  MINUTE("minute"),
  SECOND("second"),
  ROUND("round"),
  FLOOR("floor"),
  CEILING("ceiling");

  private String syntax;
  private String stringRespresentation;

  MethodOperator(String syntax)
  {
    this.syntax = syntax;
    this.stringRespresentation = syntax;
  }

  MethodOperator(String syntax, String stringRespresentation)
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
