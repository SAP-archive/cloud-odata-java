package com.sap.core.odata.core.rest;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;


public class ODataContextImpl implements ODataContext {

  private static final Logger log = LoggerFactory.getLogger(ODataContextImpl.class);

  HashMap<Class<?>, Object> contextObjects = new HashMap<Class<?>, Object>(); 

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getContextObject(Class<T> clazz) throws ODataException {
    return (T) this.contextObjects.get(clazz);
  }
  
  public void putContextObject(Class<?> clazz, Object obj) throws ODataException {
    this.contextObjects.put(clazz, obj);
  }
  
  public void log() {
    ODataContextImpl.log.debug("-- odata context -----------------");
    for (Class<?> key : this.contextObjects.keySet()) {
      ODataContextImpl.log.debug(key.getCanonicalName() + "=" + this.contextObjects.get(key).toString());
    }
    ODataContextImpl.log.debug("----------------------------------");
  }
}
