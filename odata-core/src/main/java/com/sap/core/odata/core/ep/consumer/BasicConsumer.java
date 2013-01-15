package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * Abstract basic (content type independent) consumer for reading input (from <code>content</code>).
 * 
 * @author SAP AG
 */
public abstract class BasicConsumer {

  protected BasicConsumer() throws EntityProviderException {}

  public abstract Object readPropertyValue(EdmProperty edmProperty, Object content) throws EntityProviderException;

  public abstract String readText(Object content) throws EntityProviderException;

  public abstract byte[] readBinary(String mimeType, Object content) throws EntityProviderException;
}
