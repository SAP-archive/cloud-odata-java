package com.sap.core.odata.core.uri;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.KeyPredicate;

public class KeyPredicateImpl implements KeyPredicate {

  public KeyPredicateImpl(String literal, EdmProperty property) {
    super();
    this.literal = literal;
    this.property = property;
  }

  private String literal;
  private EdmProperty property;

  @Override
  public String getLiteral() {
    return literal;
  }

  public void setValue(String value) {
    this.literal = value;
  }

  @Override
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
