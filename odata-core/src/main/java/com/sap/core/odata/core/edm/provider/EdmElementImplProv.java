package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmElement;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 */
public abstract class EdmElementImplProv extends EdmNamedImplProv implements EdmElement {

  private EdmType edmType;
  private FullQualifiedName typeName;
  private EdmFacets edmFacets;
  private EdmMapping edmMapping;

  public EdmElementImplProv(EdmImplProv edm, String name, FullQualifiedName typeName, EdmFacets edmFacets, EdmMapping edmMapping) throws EdmException {
    super(edm, name);
    this.typeName = typeName;
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null) {
      final String namespace = typeName.getNamespace();
      if (EdmSimpleTypeFacade.edmNamespace.equals(namespace)) {
        edmType = new EdmSimpleTypeFacade().getInstance(EdmSimpleTypeKind.valueOf(typeName.getName()));
      } else {
        edmType = edm.getComplexType(namespace, typeName.getName());
      }
    }

    return edmType;
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    return edmFacets.isNullable() ? EdmMultiplicity.ZERO_TO_ONE : EdmMultiplicity.ONE;
  }

  @Override
  public EdmMapping getMapping() throws EdmException {
    return edmMapping;
  }

  @Override
  public EdmFacets getFacets() throws EdmException {
    return edmFacets;
  }
}