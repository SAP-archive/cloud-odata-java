package com.sap.core.odata.core.ep.consumer;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * Abstract entity (content type dependent) consumer for reading input (from <code>content</code>).
 * 
 * @author SAP AG
 */
public abstract class EntityConsumer {

  protected EntityConsumer() throws EntityProviderException {}

//  public abstract List<Map<String, Object>> writeFeed(EdmEntitySet entitySet, Object content) throws EntityProviderException;

  public abstract Map<String, Object> readEntry(EdmEntitySet entitySet, Object content) throws EntityProviderException;

  public abstract Map<String, Object> readLink(EdmEntitySet entitySet, Object content) throws EntityProviderException;

  public abstract Map<String, Object> readProperty(EdmProperty edmProperty, Object content) throws EntityProviderException;

  public abstract List<Map<String, Object>> readLinks(EdmEntitySet entitySet, Object content) throws EntityProviderException;
}
