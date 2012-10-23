package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.rest.RuntimeDelegate;


public class UriParserFacade {

  
  public UriParserResult parse(Edm edm, List<String> pathSegments, Map<String, String> queryParameter) throws UriParserException{
    return RuntimeDelegate.getInstance().getUriParser(edm).parse(pathSegments, queryParameter);
  }
}
