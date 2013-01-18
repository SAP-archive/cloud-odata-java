package com.sap.core.odata.api.edm;

/**
 * @author SAP AG
 */
public interface EdmSimpleTypeFacade {

  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException;

}
