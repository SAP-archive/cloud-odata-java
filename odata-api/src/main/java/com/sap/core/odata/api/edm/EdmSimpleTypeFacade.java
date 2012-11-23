package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;

public interface EdmSimpleTypeFacade {

  public UriLiteral parseUriLiteral(final String uriLiteral) throws UriParserException;
  
}
