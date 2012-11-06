package com.sap.core.odata.core.rest;

import java.util.HashMap;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.service.ODataService;

public class ODataContextImpl implements ODataContext {

  protected final static String SERVICE_KEY = ODataService.class.getCanonicalName();
  
  private HashMap<String, Object> contextObjects = new HashMap<String, Object>();

  public void putContextObject(String key, Object obj) throws ODataException {
    this.contextObjects.put(key, obj);
  }
  
  public void setService(ODataService service) {
    this.contextObjects.put(ODataContextImpl.SERVICE_KEY, service);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObject(String key) throws ODataException {
    return (T) this.contextObjects.get(key);
  }

  @Override
  public ODataService getService() throws ODataException {
    return  (ODataService) this.contextObjects.get(ODataContextImpl.SERVICE_KEY);
  }
}
