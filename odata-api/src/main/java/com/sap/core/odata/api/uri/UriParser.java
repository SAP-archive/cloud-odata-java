package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

public interface UriParser {
  
  public UriParserResult parse(List<String> pathSegments, Map<String, String> queryParameters) throws UriParserException;

}
