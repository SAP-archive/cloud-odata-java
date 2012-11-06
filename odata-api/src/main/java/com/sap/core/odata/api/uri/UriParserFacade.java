package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.RuntimeDelegate;
import com.sap.core.odata.api.edm.Edm;


/**
 * Class to wrap UriParser functionality 
 * @author SAP AG
 */
public class UriParserFacade {
  
  /**
   * Parse path segments and query parameters for the given EDM
   * @param edm
   * @param pathSegments
   * @param queryParameter
   * @return {@link UriParserResult} parsed uri result
   * @throws UriParserException
   */
  public static UriParserResult parse(Edm edm, List<String> pathSegments, Map<String, String> queryParameter) throws UriParserException{
    return RuntimeDelegate.getInstance().getUriParser(edm).parse(pathSegments, queryParameter);
  }
  
  
}
