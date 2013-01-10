package com.sap.core.odata.core.ec;

import com.sap.core.odata.api.ec.BasicConsumer;
import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataRequest;

public class BasicConsumerImpl extends BasicConsumer {

  protected BasicConsumerImpl() throws EntityConsumerException {
    super();
  }

  @Override
  public Object readPropertyValue(EdmProperty edmProperty, ODataRequest request) throws EntityConsumerException {
    return null;
  }

  @Override
  public String readText(ODataRequest request) throws EntityConsumerException {
    return null;
  }

  @Override
  public byte[] readBinary(String mimeType, ODataRequest request) throws EntityConsumerException {
    return null;
  }

}
