package com.sap.core.odata.core.uri;

import com.sap.core.odata.core.edm.EdmProperty;

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
    String propertyName = this.property == null ? "null" : this.property.getName();
    String propertyType = this.property == null ? "null" : this.property.getType().getNamespace() + this.property.getType().getName();

    return "KeyPredicate: literal=" + this.literal + ", propertyName=" + propertyName + ", propertyType=" + propertyType;
  }

}
