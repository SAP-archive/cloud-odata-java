package com.sap.core.odata.api.uri.expression;

/**
 * Enumerations for all supported methods of the ODATA expression parser 
 * for ODATA version 2.0 (with some restrictions). 
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

  
  private MethodOperator(String syntax)
  {
    this.syntax = syntax;
    this.stringRespresentation = syntax;
  }

  
  private MethodOperator(String syntax, String stringRespresentation)
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
   * @return Syntax of the method as used in the URL for the $filter parameter
   */
  public String toSyntax()
  {
    return syntax;
  }

}
