package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the internal simple type "unsigned 7-bit integer"
 * @author SAP AG
 */
public class Uint7 extends AbstractSimpleType {

  private static final Uint7 instance = new Uint7();

  public static Uint7 getInstance() {
    return instance;
  }

  @Override
  public String getNamespace() throws EdmException {
    return SYSTEM_NAMESPACE;
  }

  @Override
  public boolean isCompatible(final EdmSimpleType simpleType) {
    return simpleType instanceof Bit || simpleType instanceof Uint7;
  }

  @Override
  public Number valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<?> returnType) throws EdmSimpleTypeException {
    return EdmSByte.getInstance().valueOfString(value, literalKind, facets, returnType);
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    return EdmSByte.getInstance().valueToString(value, literalKind, facets);
  }
}
