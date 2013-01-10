package com.sap.core.odata.api.ec;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * Abstract basic (content type independent) provider for writing output
 * @author SAP AG
 */
public abstract class BasicConsumer {

  protected BasicConsumer() throws EntityConsumerException {}

  public static BasicConsumer create() throws EntityConsumerException {
    return RuntimeDelegate.createBasicConsumer();
  }

  public abstract Object readPropertyValue(EdmProperty edmProperty, ODataRequest request) throws EntityConsumerException;

  public abstract String readText(ODataRequest request) throws EntityConsumerException;

  public abstract byte[] readBinary(String mimeType, ODataRequest request) throws EntityConsumerException;
}
