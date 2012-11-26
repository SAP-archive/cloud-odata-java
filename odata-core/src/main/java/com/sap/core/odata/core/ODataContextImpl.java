package com.sap.core.odata.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataPathSegment;
import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.service.ODataService;

public class ODataContextImpl implements ODataContext {

  private HashMap<String, Object> contextObjects = new HashMap<String, Object>();

  private ODataService service;

  private ODataUriInfo uriInfo;

  
  
  public void putContextObject(String key, Object obj) throws ODataException {
    this.contextObjects.put(key, obj);
  }
  
  public void setService(ODataService service) {
    this.service = service;
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

  public void setUriInfo(ODataUriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }
  
  @Override
  public ODataUriInfo getUriInfo() throws ODataException {
    return this.uriInfo;
  }


}
