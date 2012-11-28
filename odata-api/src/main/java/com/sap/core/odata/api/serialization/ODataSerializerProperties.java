package com.sap.core.odata.api.serialization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.rt.RuntimeDelegate;

public abstract class ODataSerializerProperties {

  protected Map<String, Object> data;
  protected ODataContext ctx;
  protected EdmEntitySet edmEdmEntitySet;

  /**
   * Verify that this {@link ODataSerializerProperties} is filled with all necessary and correct values.
   * If a mandatory value is missing an {@link ODataSerializationException} is thrown.
   * 
   * This method is called by each {@link ODataSerializer} within initialization.
   * 
   * @throws ODataSerializationException
   *          If a mandatory value is missing.
   */
  public abstract void verify() throws ODataSerializationException;
  
  public void setEdmEntitySet(EdmEntitySet EdmEntitySet) {
    this.edmEdmEntitySet = EdmEntitySet;
  }

  public void setContext(ODataContext ctx) {
    this.ctx = ctx;
  }

  public void setData(Map<String, Object> data) {
    this.data = new HashMap<String, Object>(data);
  }

  public ODataContext getContext() {
    return this.ctx;
  }

  public Map<String, Object> getData() {
    return Collections.unmodifiableMap(this.data);
  }

  public EdmEntitySet getEdmEntitySet() {
    return this.edmEdmEntitySet;
  }

  public static ODataSerializerProperties create() throws ODataSerializationException {
    return RuntimeDelegate.createSerializerProperties();
  }
}
