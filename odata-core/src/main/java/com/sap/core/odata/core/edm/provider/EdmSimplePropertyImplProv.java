package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

public class EdmSimplePropertyImplProv extends EdmPropertyImplProv {

  private SimpleProperty property;

  public EdmSimplePropertyImplProv(EdmImplProv edm, SimpleProperty property) throws EdmException {
    super(edm, property.getType().getFullQualifiedName(), property);
    this.property = property;
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null) {
      edmType = EdmSimpleTypeFacadeImpl.getEdmSimpleType(property.getType());
      if (edmType == null) {
        throw new EdmException(EdmException.COMMON);
      }
    }
    return edmType;
  }

  @Override
  public boolean isSimple() {
    return true;
  }
}
