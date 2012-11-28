package com.sap.core.odata.api.serialization;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.rt.RuntimeDelegate;

public abstract class ODataSerializer {

  protected final ODataSerializerProperties properties;

  protected ODataSerializer(ODataSerializerProperties properties) throws ODataSerializationException {
    this.properties = properties;
    this.properties.verify();
  }
  
  public static ODataSerializer create(Format atom, ODataSerializerProperties properties) throws ODataSerializationException {
    return RuntimeDelegate.createSerializer(atom, properties);
  }

  public abstract InputStream serialize() throws ODataSerializationException;

  public abstract void serializeInto(OutputStream stream) throws ODataSerializationException;

  protected EdmEntitySet getEdmEntitySet() {
    return properties.getEdmEntitySet();
  }

  protected ODataContext getContext() {
    return properties.getContext();
  }

  protected Map<String, Object> getData() {
    return properties.getData();
  }
}
