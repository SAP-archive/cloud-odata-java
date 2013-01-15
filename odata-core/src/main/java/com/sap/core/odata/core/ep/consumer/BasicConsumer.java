package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;

public class BasicConsumer {

  protected BasicConsumer() throws EntityProviderException {
    super();
  }

  public Object readPropertyValue(EdmProperty edmProperty, Object content) throws EntityProviderException {
    return null;
  }

  public String readText(Object content) throws EntityProviderException {
    return null;
  }

  public byte[] readBinary(String mimeType, Object content) throws EntityProviderException {
    return null;
  }

}
