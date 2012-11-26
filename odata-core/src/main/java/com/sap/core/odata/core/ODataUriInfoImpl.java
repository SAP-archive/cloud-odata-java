package com.sap.core.odata.core;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.processor.ODataPathSegment;
import com.sap.core.odata.api.processor.ODataUriInfo;

public class ODataUriInfoImpl implements ODataUriInfo {

  private List<ODataPathSegment> precedingPathSegment = Collections.emptyList();
  private List<ODataPathSegment> odataPathSegment = Collections.emptyList();
  private URI baseUri;
  
  public void setODataPathSegment(List<ODataPathSegment> odataPathSegement) {
    this.odataPathSegment = odataPathSegement;
  }

  public void setPrecedingPathSegment(List<ODataPathSegment> precedingPathSegement) {
    this.precedingPathSegment = precedingPathSegement;
  }

  public void setBaseUri(URI uri) {
    this.baseUri = uri;
  }
  
  @Override
  public List<ODataPathSegment> getPrecedingPathSegmentList() {
    return Collections.unmodifiableList(this.precedingPathSegment);
  }

  @Override
  public List<ODataPathSegment> getODataPathSegmentList() {
    return Collections.unmodifiableList(this.odataPathSegment);
  }

  @Override
  public URI getBaseUri() {
    return this.baseUri;
  }
}
