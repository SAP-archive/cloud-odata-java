package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;

public class BasicConsumerImpl extends BasicConsumer {

  protected BasicConsumerImpl() throws EntityProviderException {
    super();
  }

  @Override
  public Object readPropertyValue(EdmProperty edmProperty, Object content) throws EntityProviderException {
    return null;
  }

  @Override
  public String readText(Object content) throws EntityProviderException {
    return null;
  }

  @Override
  public byte[] readBinary(String mimeType, Object content) throws EntityProviderException {
    return null;
  }

}
