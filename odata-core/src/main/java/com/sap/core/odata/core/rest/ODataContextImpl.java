package com.sap.core.odata.core.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.service.ODataService;

public class ODataContextImpl implements ODataContext {

  private HashMap<String, Object> contextObjects = new HashMap<String, Object>();

  private ODataService service;

  private List<String> precedingPathSegment = Collections.emptyList();
  private List<String> odataPathSegment = Collections.emptyList();
  
  
  public void putContextObject(String key, Object obj) throws ODataException {
    this.contextObjects.put(key, obj);
  }
  
  public void setService(ODataService service) {
    this.service = service;
  }
  
  public void setODataPathSegment(List<String> odataPathSegement) {
    this.odataPathSegment = odataPathSegement;
  }

  public void setPrecedingPathSegment(List<String> precedingPathSegement) {
    this.precedingPathSegment = precedingPathSegement;
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
  public List<String> getPrecedingPathSegment() {
    return Collections.unmodifiableList(this.precedingPathSegment);
  }
  
  @Override
  public List<String> getODataPathSegment() {
    return Collections.unmodifiableList(this.odataPathSegment);
  }
}
