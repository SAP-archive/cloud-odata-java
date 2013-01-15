package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.core.commons.ContentType;

public class ConsumerFactory {

  public static BasicConsumer createBasicConsumer() throws EntityProviderException {
    return new BasicConsumerImpl();
  }

  public static EntityConsumer createEntityConsumer(String contentType) throws EntityProviderException {
    return createEntityConsumer(ContentType.create(contentType));
  }
  
  public static EntityConsumer createEntityConsumer(ContentType contentType) throws EntityProviderException {
    try {
      EntityConsumer consumer;

      switch (contentType.getODataFormat()) {
      case ATOM:
      case XML:
        consumer = new XmlEntityConsumer();
        break;
      case JSON:
        throw new ODataNotImplementedException();
      default:
        throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(contentType));
      }

      return consumer;
    } catch (ODataNotImplementedException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (ODataNotAcceptableException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

}
