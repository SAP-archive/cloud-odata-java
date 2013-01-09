package com.sap.core.odata.api.ec;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.rt.RuntimeDelegate;

public abstract class EntityConsumer {

  protected EntityConsumer() throws EntityConsumerException {}

  public static EntityConsumer create(String contentType) throws EntityConsumerException {
    return RuntimeDelegate.createEntityConsumer(contentType);
  }
  
//  public abstract List<Map<String, Object>> writeFeed(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException;

  public abstract Map<String, Object> readEntry(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException;

  public abstract Map<String, Object> readLink(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException;

  public abstract Object readProperty(EdmProperty edmProperty, ODataRequest request) throws EntityConsumerException;

  public abstract List<Map<String, Object>> readLinks(EdmEntitySet entitySet, ODataRequest request) throws EntityConsumerException;
}
