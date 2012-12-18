package com.sap.core.odata.core.edm;


/**
 * Implementation of the simple type Null
 * @author SAP AG
 */
public class EdmNull extends AbstractSimpleType {

  private static final EdmNull instance = new EdmNull();

  public static EdmNull getInstance() {
    return instance;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj == null;
  }

  @Override
  public String toUriLiteral(final String literal) {
    return "null";
  }
}
