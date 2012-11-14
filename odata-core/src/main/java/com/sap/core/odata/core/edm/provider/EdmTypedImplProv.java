package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 */
public class EdmTypedImplProv extends EdmNamedImplProv implements EdmTyped {

  private EdmType edmType;
  private FullQualifiedName typeName;
  private EdmMultiplicity multiplicity;

  public EdmTypedImplProv(EdmImplProv edm, String name, FullQualifiedName typeName, EdmMultiplicity multiplicity) throws EdmException {
    super(edm, name);
    this.typeName = typeName;
    this.multiplicity = multiplicity;
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null) {
      final String namespace = typeName.getNamespace();
      if (EdmSimpleTypeFacade.edmNamespace.equals(typeName.getNamespace())) {
        edmType = EdmSimpleTypeFacade.getInstance(EdmSimpleTypeKind.valueOf(typeName.getName()));
      } else {
        try {
          edmType = edm.getComplexType(namespace, typeName.getName());
        } catch (EdmException e) {
          edmType = null;
        }
        if (edmType == null)
          edmType = edm.getEntityType(namespace, typeName.getName());
      }
    }
    return edmType;
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    return multiplicity;
  }
}