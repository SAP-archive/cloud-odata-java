package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.uri.UriSyntaxException;

public interface EdmSimpleTypeFacade {

  public EdmLiteral parseUriLiteral(final String uriLiteral) throws UriSyntaxException;
  
}
