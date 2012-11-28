package com.sap.core.odata.core.serializer;

import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.api.serialization.ODataSerializerProperties;

public class ODataSerializerFactory {

  public static ODataSerializer create(Format format, ODataSerializerProperties properties) throws ODataSerializationException {
    try {
      ODataSerializer serializer;

      switch (format) {
      case ATOM:
//        serializer = new AtomEntrySerializer(properties);
        serializer = new AtomSerializer(properties);
        break;
      case JSON:
        throw new ODataNotImplementedException();
      case XML:
        throw new ODataNotImplementedException();
      default:
        throw new ODataNotImplementedException();
      }

      return serializer;
    } catch (ODataNotImplementedException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  public static ODataSerializerProperties createProperties() {
    return new BasicSerializerProperties();
  }

}
