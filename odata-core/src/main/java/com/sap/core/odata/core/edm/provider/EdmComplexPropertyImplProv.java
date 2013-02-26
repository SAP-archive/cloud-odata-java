package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.provider.ComplexProperty;

/**
 * @author SAP AG
 */
public class EdmComplexPropertyImplProv extends EdmPropertyImplProv {

  private ComplexProperty property;

  public EdmComplexPropertyImplProv(final EdmImplProv edm, final ComplexProperty property) throws EdmException {
    super(edm, property.getType(), property);
    this.property = property;
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null)
      edmType = edm.getComplexType(property.getType().getNamespace(), property.getType().getName());
    if (edmType == null)
      throw new EdmException(EdmException.PROVIDERPROBLEM);
    return edmType;
  }

  @Override
  public boolean isSimple() {
    return false;
  }
}
