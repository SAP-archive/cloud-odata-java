package com.sap.core.odata.core.ep;

import java.io.InputStream;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * Interface for basic (content type independent) provider methods.
 * @author SAP AG
 */
public interface BasicEntityProviderInterface {

  public abstract Object readPropertyValue(EdmProperty edmProperty, InputStream content) throws EntityProviderException;

  /**
   * Write service document based on given {@link Edm} and <code>service root</code> as
   * content type "<code>application/atomsvc+xml; charset=utf-8</code>".
   * 
   * @param edm
   * @param serviceRoot
   * @return
   * @throws EntityProviderException
   */
  public abstract ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException;

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
