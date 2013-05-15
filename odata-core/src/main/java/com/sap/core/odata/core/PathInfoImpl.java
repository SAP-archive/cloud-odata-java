package com.sap.core.odata.core;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.PathSegment;

/**
 * @author SAP AG
 */
public class PathInfoImpl implements PathInfo {

  private List<PathSegment> precedingPathSegment = Collections.emptyList();
  private List<PathSegment> odataPathSegment = Collections.emptyList();
  private URI serviceRoot;
  private URI requestUri;

  public void setODataPathSegment(final List<PathSegment> odataPathSegement) {
    odataPathSegment = odataPathSegement;
  }

  public void setPrecedingPathSegment(final List<PathSegment> precedingPathSegement) {
    precedingPathSegment = precedingPathSegement;
  }

  public void setServiceRoot(final URI uri) {
    serviceRoot = uri;
  }

  @Override
  public List<PathSegment> getPrecedingSegments() {
    return Collections.unmodifiableList(precedingPathSegment);
  }

  @Override
  public List<PathSegment> getODataSegments() {
    return Collections.unmodifiableList(odataPathSegment);
  }

  @Override
  public URI getServiceRoot() {
    return serviceRoot;
  }

  @Override
  public URI getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(final URI requestUri) {
    this.requestUri = requestUri;
  }
}
