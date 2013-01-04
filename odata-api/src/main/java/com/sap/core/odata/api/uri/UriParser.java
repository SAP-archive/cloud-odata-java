package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.rt.RuntimeDelegate;


/**
 * Class to wrap UriParser functionality 
 * @author SAP AG
 */
public abstract class UriParser {
  
  /**
   * Parse path segments and query parameters for the given EDM
   * @param edm
   * @param pathSegments
   * @param queryParameter
   * @return {@link UriInfo} parsed uri result
   * @throws UriSyntaxException
   */
  public static UriInfo parse(Edm edm, List<PathSegment> pathSegments, Map<String, String> queryParameter) throws ODataException {
    return RuntimeDelegate.getUriParser(edm).parse(pathSegments, queryParameter);
  }

  /**
   * Parse path segments and query parameters.
   * 
   * @param pathSegments
   * @param queryParameters
   * @return {@link UriInfo} parsed uri result
   * @throws UriSyntaxException
   */
  public abstract UriInfo parse(List<PathSegment> pathSegments, Map<String, String> queryParameters) throws UriSyntaxException, UriNotMatchingException, EdmException;  
}
