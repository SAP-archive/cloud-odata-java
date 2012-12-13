package com.sap.core.odata.core.ep;

import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataContext;

public class EntityProviderFactory {

  public static ODataEntityProvider create(Format format) throws ODataEntityProviderException {
    try {
      ODataEntityProvider provider;

      switch (format) {
      case ATOM:
      case XML:
        provider = new AtomEntityProvider();
        break;
      case JSON:
        throw new ODataNotImplementedException();
      default:
        throw new ODataNotImplementedException();
      }

      return provider;
    } catch (ODataNotImplementedException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }
}
