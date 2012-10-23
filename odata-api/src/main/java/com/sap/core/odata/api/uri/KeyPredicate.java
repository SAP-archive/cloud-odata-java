package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmProperty;


public class KeyPredicate {

  public KeyPredicate(String literal, EdmProperty property) {
    super();
    this.literal = literal;
    this.property = property;
  }

  private String literal;
  private EdmProperty property;

  public String getLiteral() {
    return literal;
  }

  public void setValue(String value) {
    this.literal = value;
  }

  public EdmProperty getProperty() {
    return property;
  }

  public void setProperty(EdmProperty property) {
    this.property = property;
  }

  @Override
  public String toString() {
    return "KeyPredicate: literal=" + this.literal + ", propertyName=" + this.property;
  }

}
