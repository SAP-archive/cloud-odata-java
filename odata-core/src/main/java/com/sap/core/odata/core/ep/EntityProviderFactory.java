package com.sap.core.odata.core.ep;

import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.core.enums.ContentType;

public class EntityProviderFactory {

  public static EntityProvider create(String contentType) throws EntityProviderException {
    return create(ContentType.create(contentType));
  }
  
  public static EntityProvider create(ContentType contentType) throws EntityProviderException {
    try {
      EntityProvider provider;

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
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (ODataNotAcceptableException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
