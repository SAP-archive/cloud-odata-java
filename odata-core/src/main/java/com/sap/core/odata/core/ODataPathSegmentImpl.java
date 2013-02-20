package com.sap.core.odata.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.uri.PathSegment;

/**
 * @author SAP AG
 */
public class ODataPathSegmentImpl implements PathSegment {

  private String path;
  private Map<String, List<String>> matrixParameter;

  public ODataPathSegmentImpl(String path, Map<String, List<String>> matrixParameters) {
    this.path = path;

    Map<String, List<String>> unmodifiableMap = new HashMap<String, List<String>>();
    if (matrixParameters != null) {
      for (String key : matrixParameters.keySet()) {
        List<String> values = Collections.unmodifiableList(matrixParameters.get(key));
        unmodifiableMap.put(key, values);
      }
    }

    this.matrixParameter = Collections.unmodifiableMap(unmodifiableMap);
  }

  @Override
  public String getPath() {
    return this.path;
  }

  @Override
  public Map<String, List<String>> getMatrixParameters() {
    return this.matrixParameter;
  }

}
