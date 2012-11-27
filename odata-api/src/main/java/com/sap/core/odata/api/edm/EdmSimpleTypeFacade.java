package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.uri.EdmLiteral;
import com.sap.core.odata.api.uri.UriParserException;

public interface EdmSimpleTypeFacade {

  public EdmLiteral parseUriLiteral(final String uriLiteral) throws UriParserException;
  
}
