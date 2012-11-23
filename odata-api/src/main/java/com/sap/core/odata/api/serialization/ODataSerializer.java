package com.sap.core.odata.api.serialization;

import java.io.InputStream;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.rt.RuntimeDelegate;

public abstract class ODataSerializer {

  private Map<String, Object> data;
  private ODataContext ctx;
  private EdmEntitySet edmEdmEntitySet;

  public static ODataSerializer create(Format atom) throws ODataSerializationException {
    return RuntimeDelegate.createSerializer(atom);
  }

  public abstract InputStream serialize() throws ODataSerializationException;

  public void setEdmEntitySet(EdmEntitySet EdmEntitySet) {
    this.edmEdmEntitySet = EdmEntitySet;
  }

  public void setContext(ODataContext ctx) {
    this.ctx = ctx;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  protected ODataContext getContext() {
    return this.ctx;
  }

  protected Map<String, Object> getData() {
    return this.data;
  }

  protected EdmEntitySet getEdmEntitySet() {
    return this.edmEdmEntitySet;
  }

}
