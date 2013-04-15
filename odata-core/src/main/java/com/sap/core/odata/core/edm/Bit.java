/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.edm;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;

/**
 * Implementation of the internal simple type Bit
 * @author SAP AG
 */
public class Bit extends AbstractSimpleType {

  private static final Bit instance = new Bit();

  public static Bit getInstance() {
    return instance;
  }

  @Override
  public String getNamespace() throws EdmException {
    return SYSTEM_NAMESPACE;
  }

  @Override
  public Class<?> getDefaultType() {
    return Byte.class;
  }

  @Override
  public <T> T valueOfString(final String value, final EdmLiteralKind literalKind, final EdmFacets facets, final Class<T> returnType) throws EdmSimpleTypeException {
    return EdmSByte.getInstance().valueOfString(value, literalKind, facets, returnType);
  }

  @Override
  public String valueToString(final Object value, final EdmLiteralKind literalKind, final EdmFacets facets) throws EdmSimpleTypeException {
    return EdmSByte.getInstance().valueToString(value, literalKind, facets);
  }
}
