package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmSimpleType;

/**
 * UriLiteral representation
 * @author SAP AG
 */
public final class UriLiteral {
  private EdmSimpleType type;
  private String literal;

  /**
   * @param {@link EdmSimpleType} type
   * @param {@link String} literal
   */
  public UriLiteral(final EdmSimpleType type, final String literal) {
    this.type = type;
    this.literal = literal;
  }

  /**
   * @return {@link EdmSimpleType} object
   */
  public EdmSimpleType getType() {
    return type;
  }

  /**
   * @return String literal
   */
  public String getLiteral() {
    return literal;
  }

  @Override
  public String toString() {
    return "UriLiteral: type=" + type + ", literal=" + literal;
  }
}
