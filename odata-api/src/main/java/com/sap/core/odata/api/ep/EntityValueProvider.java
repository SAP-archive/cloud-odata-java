package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * Abstract provider for writing output
 * @author SAP AG
 */
public abstract class EntityValueProvider {

  protected EntityValueProvider() throws EntityProviderException {}

  public static EntityValueProvider create() throws EntityProviderException {
    return RuntimeDelegate.createEntityValueProvider();
  }

  /**
   * Write property as content type <code>application/octet-stream</code> or <code>text/plain</code>.
   * 
   * @param edmProperty
   * @param value
   * @return
   * @throws EntityProviderException
   */
  public abstract ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws EntityProviderException;

  /**
   * Write text value as content type <code>text/plain</code>.
   * 
   * @param value
   * @return
   * @throws EntityProviderException
   */
  public abstract ODataResponse writeText(String value) throws EntityProviderException;
  
  /**
   * Write binary content with content type header set to given <code>mime type</code> parameter.
   * 
   * @param mimeType mime type which is written and used as content type header information.
   * @param data which is written to {@link ODataResponse}.
   * @return resulting {@link ODataResponse} with written binary content.
   * @throws EntityProviderException
   */
  public abstract ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException;
}
