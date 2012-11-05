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

public abstract class EdmElementImplProv extends EdmNamedImplProv implements EdmElement {

  private EdmType edmType;
  private String namespace;
  private EdmFacets edmFacets;
  private EdmMapping edmMapping;

  public EdmElementImplProv(EdmImplProv edm, FullQualifiedName fqName, EdmFacets edmFacets, EdmMapping edmMapping) throws EdmException {
    super(edm, fqName.getName());
    this.namespace = fqName.getNamespace();
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null) {
      if (EdmSimpleTypeFacade.edmNamespace.equals(namespace)) {
        edmType = new EdmSimpleTypeFacade().getInstance(EdmSimpleTypeKind.valueOf(getName()));
      } else {
        edmType = edm.getComplexType(namespace, getName());
      }
    }

    return edmType;
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    if (!edmFacets.isNullable()) {
      return EdmMultiplicity.ONE;
    } else {
      return EdmMultiplicity.ZERO_TO_ONE;
    }
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