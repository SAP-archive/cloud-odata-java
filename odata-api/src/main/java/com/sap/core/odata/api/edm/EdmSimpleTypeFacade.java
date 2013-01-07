package com.sap.core.odata.api.edm;


public interface EdmSimpleTypeFacade {

  public EdmLiteral parseUriLiteral(final String uriLiteral) throws EdmLiteralException;
  
}
