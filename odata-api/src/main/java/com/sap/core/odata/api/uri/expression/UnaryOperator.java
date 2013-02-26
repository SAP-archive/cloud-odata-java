package com.sap.core.odata.api.uri.expression;

/**
 * Enumerations for supported unary operators of the OData expression parser
 * for OData version 2.0
 * @author SAP AG 
 */
public enum UnaryOperator {
  MINUS("-", "negation"), NOT("not");

  private String syntax;
  private String stringRespresentation;

  private UnaryOperator(final String syntax) {
    this.syntax = syntax;
    stringRespresentation = syntax;
  }

  private UnaryOperator(final String syntax, final String stringRespresentation) {
    this.syntax = syntax;
    this.stringRespresentation = stringRespresentation;
  }

  /** 
   * @return Methods name for usage in in text
   */
  @Override
  public String toString() {
    return stringRespresentation;
  }

  /**
   * @return Syntax of the unary operator as used in the URL. 
   */
  public String toUriLiteral() {
    return syntax;
  }
}