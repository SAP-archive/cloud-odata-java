package com.sap.core.odata.api.uri.expression;

/**
 * Enumerations for supported binary operators of the ODATA expression parser 
 * for ODATA version 2.0 (with some restrictions)
 * @author SAP AG
*/
public enum BinaryOperator {
  AND("and"), OR("or"), EQ("eq"), NE("ne"), LT("lt"), LE("le"), GT("gt"), GE("ge"), ADD("add"), SUB("sub"), MUL("mul"), DIV("div"), MODULO("mod"),

  /**
   * Property access operator. E.g. $filter=address/city eq "Sydney"
   */
  PROPERTY_ACCESS("/", "property access");

  private String uriSyntax;
  private String stringRespresentation;

  private BinaryOperator(final String uriSyntax) {
    this.uriSyntax = uriSyntax;
    stringRespresentation = uriSyntax;
  }

  private BinaryOperator(final String syntax, final String stringRespresentation) {
    uriSyntax = syntax;
    this.stringRespresentation = stringRespresentation;
  }

  /** 
   * @return Operator name for usage in text
   */
  @Override
  public String toString() {
    return stringRespresentation;
  }

  /**
   * @return URI literal of the binary operator as used in the URL. 
   */
  public String toUriLiteral() {
    return uriSyntax;
  }
}