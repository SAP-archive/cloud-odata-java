package com.sap.core.odata.core;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.uri.PathSegment;

public class ODataUriInfoImpl implements ODataUriInfo {

  private List<PathSegment> precedingPathSegment = Collections.emptyList();
  private List<PathSegment> odataPathSegment = Collections.emptyList();
  private URI baseUri;

  public void setODataPathSegment(List<PathSegment> odataPathSegement) {
    this.odataPathSegment = odataPathSegement;
  }

  public void setPrecedingPathSegment(List<PathSegment> precedingPathSegement) {
    this.precedingPathSegment = precedingPathSegement;
  }

  public void setBaseUri(URI uri) {
    this.baseUri = uri;
  }

  @Override
  public List<PathSegment> getPrecedingPathSegmentList() {
    return Collections.unmodifiableList(this.precedingPathSegment);
  }

  @Override
  public List<PathSegment> getODataPathSegmentList() {
    return Collections.unmodifiableList(this.odataPathSegment);
  }

  @Override
  public URI getBaseUri() {
    return this.baseUri;
  }
}
