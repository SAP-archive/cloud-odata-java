package com.sap.core.odata.core.processor;

import java.util.Map;

import com.sap.core.odata.api.processor.PathSegment;

public class PathSegmentImpl implements PathSegment {

  private String path;
  private Map<String, String> matrixParameter;

  public PathSegmentImpl(String path, Map<String, String> matrixParameters) {
    this.path = path;
    this.matrixParameter = matrixParameters;
  }
  
  @Override
  public String getPath() {
    return this.path;
  }

  @Override
  public Map<String, String> getMatrixParameters() {
    return null;
  }

}
