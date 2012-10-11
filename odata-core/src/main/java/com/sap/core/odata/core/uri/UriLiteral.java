package com.sap.core.odata.core.uri;

import com.sap.core.odata.core.edm.EdmSimpleType;

public class UriLiteral {
  private EdmSimpleType type;
  private String literal;

  public UriLiteral() {
  }

  public UriLiteral(final EdmSimpleType type, final String literal) {
    this.type = type;
    this.literal = literal;
  }

  public EdmSimpleType getType() {
    return type;
  }

  public void setType(EdmSimpleType type) {
    this.type = type;
  }

  public String getLiteral() {
    return literal;
  }

  public void setLiteral(String literal) {
    this.literal = literal;
  }

  @Override
  public String toString() {
    return "UriLiteral: type=" + type + ", literal=" + literal;
  }
}
