package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * EdmType holds the namespace of a given type and its type as {@link EdmTypeKind}.
 * @author SAP AG
 */
public interface EdmType extends EdmNamed {

  /**
   * Namespace of this {@link EdmType}
   * @return namespace as String
   * @throws EdmException
   */
  String getNamespace() throws EdmException;

  /**
   * @return {@link EdmTypeKind} of this {@link EdmType}
   */
  EdmTypeKind getKind();
}