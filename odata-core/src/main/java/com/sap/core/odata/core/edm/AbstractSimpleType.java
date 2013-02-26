package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Abstract implementation of the EDM simple-type interface
 * @author SAP AG
 */
public abstract class AbstractSimpleType implements EdmSimpleType {

  @Override
  public boolean equals(final Object obj) {
    return this == obj || getClass().equals(obj.getClass());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String getNamespace() throws EdmException {
    return EDM_NAMESPACE;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.SIMPLE;
  }

  @Override
  public String getName() throws EdmException {
    final String name = getClass().getSimpleName();
    if (name.startsWith(EDM_NAMESPACE)) {
      return name.substring(3);
    } else {
      return name;
    }
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return equals(simpleType);
  }

  @Override
  public boolean validate(final String value, final EdmLiteralKind literalKind, final EdmFacets facets) {
    try {
      valueOfString(value, literalKind, facets, getDefaultType());
      return true;
    } catch (EdmSimpleTypeException e) {
      return false;
    }
  }

  @Override
  public String toUriLiteral(final String literal) throws EdmSimpleTypeException {
    return literal;
  }

  /**
   * Checks whether the metadata allow a null literal.
   * @param facets   metadata given as {@link EdmFacets}
   * @throws EdmSimpleTypeException  if the metadata constraints are not met
   */
  protected static final void checkNullLiteralAllowed(final EdmFacets facets) throws EdmSimpleTypeException {
    if (facets != null && facets.isNullable() != null && !facets.isNullable()) {
      throw new EdmSimpleTypeException(EdmSimpleTypeException.LITERAL_NULL_NOT_ALLOWED);
    }
  }

  /**
   * Returns <code>null</code> or the default literal as specified in the metadata.
   * @param facets  metadata given as {@link EdmFacets}
   * @throws EdmSimpleTypeException  if the metadata constraints are not met
   */
  protected static final String getNullOrDefaultLiteral(final EdmFacets facets) throws EdmSimpleTypeException {
    if (facets == null) {
      return null;
    } else if (facets.getDefaultValue() == null) {
      if (facets.isNullable() == null || facets.isNullable()) {
        return null;
      } else {
        throw new EdmSimpleTypeException(EdmSimpleTypeException.VALUE_NULL_NOT_ALLOWED);
      }
    } else {
      return facets.getDefaultValue();
    }
  }
}
