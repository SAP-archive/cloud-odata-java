package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.rest.RuntimeDelegate;


/**
 * @author SAP AG
 * Class to wrap UriParser functionality 
 *
 */
public class UriParserFacade {
  
  /**
   * Parse path segments and query parameters for the given edm
   * @param edm
   * @param pathSegments
   * @param queryParameter
   * @return {@link UriParserResult} parsed uri result
   * @throws UriParserException
   */
  public UriParserResult parse(Edm edm, List<String> pathSegments, Map<String, String> queryParameter) throws UriParserException{
    return RuntimeDelegate.getInstance().getUriParser(edm).parse(pathSegments, queryParameter);
  }
}
