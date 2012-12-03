package com.sap.core.odata.core.ep;

import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.ep.ODataSerializationException;
import com.sap.core.odata.api.ep.ODataSerializer;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataContext;

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
