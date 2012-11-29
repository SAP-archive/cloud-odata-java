package com.sap.core.odata.core.serializer;

import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;

public class ODataSerializerFactory {

  public static ODataSerializer create(Format format, ODataContext ctx) throws ODataSerializationException {
    try {
      ODataSerializer serializer;

      switch (format) {
      case ATOM:
      case XML:
        serializer = new AtomSerializer(ctx);
        break;
      case JSON:
        throw new ODataNotImplementedException();
      default:
        throw new ODataNotImplementedException();
      }

      return serializer;
    } catch (ODataNotImplementedException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }
}
