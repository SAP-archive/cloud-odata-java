package com.sap.core.odata.api.serialization;

import java.io.InputStream;

import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.rt.RuntimeDelegate;

public abstract class ODataSerializer {

  public static ODataSerializer create(Format atom) throws ODataSerializationException {
    return RuntimeDelegate.createSerializer(atom);
  }

  public abstract InputStream serialize() throws ODataSerializationException;

}
