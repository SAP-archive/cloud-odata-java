package com.sap.core.odata.api.serialization;

import java.io.InputStream;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.rt.RuntimeDelegate;

public abstract class ODataSerializer {
 
  private final ODataContext context;

  protected ODataSerializer(ODataContext ctx) throws ODataSerializationException {
    this.context = ctx;
  }
  
  public static ODataSerializer create(Format atom, ODataContext ctx) throws ODataSerializationException {
    return RuntimeDelegate.createSerializer(atom, ctx);
  }

  public abstract InputStream serializeEntry(EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException;

  protected ODataContext getContext() {
    return this.context;
  }

}
