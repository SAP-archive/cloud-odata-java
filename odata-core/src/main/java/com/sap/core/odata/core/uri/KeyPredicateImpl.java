package com.sap.core.odata.core.uri;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.KeyPredicate;

public class KeyPredicateImpl implements KeyPredicate {

  public KeyPredicateImpl(final String literal, final EdmProperty property) {
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

  public void setValue(final String value) {
    literal = value;
  }

  @Override
  public EdmProperty getProperty() {
    return property;
  }

  public void setProperty(final EdmProperty property) {
    this.property = property;
  }

  @Override
  public String toString() {
    return "KeyPredicate: literal=" + literal + ", propertyName=" + property;
  }

}
