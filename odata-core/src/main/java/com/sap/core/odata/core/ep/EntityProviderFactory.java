package com.sap.core.odata.core.ep;

import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.core.enums.ContentType;

public class EntityProviderFactory {

  public static ODataEntityProvider create(String contentType) throws ODataEntityProviderException {
    return create(ContentType.create(contentType));
  }
  
  public static ODataEntityProvider create(ContentType contentType) throws ODataEntityProviderException {
    try {
      ODataEntityProvider provider;

      switch (contentType.getODataFormat()) {
      case ATOM:
      case XML:
        provider = new AtomEntityProvider();
        break;
      case JSON:
        throw new ODataNotImplementedException();
      default:
        throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(contentType));
      }

      return provider;
    } catch (ODataNotImplementedException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    } catch (ODataNotAcceptableException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }
}
