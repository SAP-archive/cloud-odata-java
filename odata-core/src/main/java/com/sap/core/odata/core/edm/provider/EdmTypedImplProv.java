package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

/**
 * @author SAP AG
 */
public class EdmTypedImplProv extends EdmNamedImplProv implements EdmTyped {

  protected EdmType edmType;
  private FullQualifiedName typeName;
  private EdmMultiplicity multiplicity;

  public EdmTypedImplProv(final EdmImplProv edm, final String name, final FullQualifiedName typeName, final EdmMultiplicity multiplicity) throws EdmException {
    super(edm, name);
    this.typeName = typeName;
    this.multiplicity = multiplicity;
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null) {
      final String namespace = typeName.getNamespace();
      if (EdmSimpleType.EDM_NAMESPACE.equals(typeName.getNamespace())) {
        edmType = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.valueOf(typeName.getName()));
      } else {
        edmType = edm.getComplexType(namespace, typeName.getName());
      }
      if (edmType == null) {
        edmType = edm.getEntityType(namespace, typeName.getName());
      }

      if (edmType == null) {
        throw new EdmException(EdmException.COMMON);
      }

    }
    return edmType;
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    return multiplicity;
  }
}