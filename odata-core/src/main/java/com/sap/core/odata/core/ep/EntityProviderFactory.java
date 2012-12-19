package com.sap.core.odata.core.ep;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;

public class EntityProviderFactory {

  public static ODataEntityProvider create(ContentType contentType) throws ODataEntityProviderException {
    try {
      ODataEntityProvider provider;

      provider = new AtomEntityProvider();

      switch (contentType.getODataFormat()) {
      case ATOM:
      case XML:
        provider = new AtomEntityProvider();
        break;
      case JSON:
        throw new ODataNotImplementedException();
      default:
//        throw new ODataNotImplementedException();
      }

      return provider;
    } catch (ODataNotImplementedException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }
}
