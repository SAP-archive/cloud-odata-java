package com.sap.core.odata.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataPathSegment;
import com.sap.core.odata.api.service.ODataService;

public class ODataContextImpl implements ODataContext {

  private HashMap<String, Object> contextObjects = new HashMap<String, Object>();

  private ODataService service;

  private List<ODataPathSegment> precedingPathSegment = Collections.emptyList();
  private List<ODataPathSegment> odataPathSegment = Collections.emptyList();
  
  
  public void putContextObject(String key, Object obj) throws ODataException {
    this.contextObjects.put(key, obj);
  }
  
  public void setService(ODataService service) {
    this.service = service;
  }
  
  public void setODataPathSegment(List<ODataPathSegment> odataPathSegement) {
    this.odataPathSegment = Collections.unmodifiableList(odataPathSegement);
  }

  public void setPrecedingPathSegment(List<ODataPathSegment> precedingPathSegement) {
    this.precedingPathSegment = Collections.unmodifiableList(precedingPathSegement);
  }


  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObject(String key) throws ODataException {
    return (T) this.contextObjects.get(key);
  }

  @Override
  public ODataService getService() throws ODataException {
    return  this.service;
  }

  @Override
  public List<ODataPathSegment> getPrecedingPathSegmentList() {
    return Collections.unmodifiableList(this.precedingPathSegment);
  }
  
  @Override
  public List<ODataPathSegment> getODataPathSegmentList() {
    return Collections.unmodifiableList(this.odataPathSegment);
  }
}
