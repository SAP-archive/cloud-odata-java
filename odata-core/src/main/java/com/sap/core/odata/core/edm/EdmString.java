package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Implementation of the EDM simple type String
 * @author SAP AG
 */
public class EdmString implements EdmSimpleType {

  private static final EdmString instance = new EdmString();

  private EdmString() {

  }

  public static EdmString getInstance() {
    return instance;
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof EdmString;
  }

  @Override
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeKind.edmNamespace;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.SIMPLE;
  }

  @Override
  public String getName() throws EdmException {
    return EdmSimpleTypeKind.String.toString();
  }

  @Override
  public boolean isCompatible(EdmSimpleType simpleType) {
    return simpleType instanceof EdmString;
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      valueOfString(value, literalKind, facets);
      return true;
    } catch (EdmSimpleTypeException e) {
      return false;
    }
  }

  @Override
  public Object valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (literalKind == EdmLiteralKind.URI)
      return null;
    else
      return value;
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    if (literalKind == EdmLiteralKind.URI)
      return toUriLiteral((String) value);
    else
      return (String) value;
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return "'" + literal + "'";
  }

}
