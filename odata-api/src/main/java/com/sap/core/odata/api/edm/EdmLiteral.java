package com.sap.core.odata.api.edm;

/** 
 * UriLiteral representation
 * @author SAP AG
 */
public final class EdmLiteral {
  private EdmSimpleType type;
  private String literal;

  /**
   * @param type {@link EdmSimpleType} simple type
   * @param literal {@link String} literal in default (<em>not</em> URI) representation
   * @see EdmLiteralKind
   */
  public EdmLiteral(final EdmSimpleType type, final String literal) {
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
   * @return {@link String} literal in default (<em>not</em> URI) representation
   */
  public String getLiteral() {
    return literal;
  }

  @Override
  public String toString() {
    return "UriLiteral: type=" + type + ", literal=" + literal;
  }
}
