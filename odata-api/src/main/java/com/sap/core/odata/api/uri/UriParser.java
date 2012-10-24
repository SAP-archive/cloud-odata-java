package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

/**
 * @author SAP AG
 * Uri parser interface
 */
public interface UriParser {
  
  /**
   * parses the given path segments and query parameter
   * @param pathSegments to be parsed
   * @param queryParameters to be parsed
   * @return {@link UriParserResult} the parsing result
   * @throws UriParserException
   */
  public UriParserResult parse(List<String> pathSegments, Map<String, String> queryParameters) throws UriParserException;

}
