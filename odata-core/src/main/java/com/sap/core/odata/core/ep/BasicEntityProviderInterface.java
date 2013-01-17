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

  /**
   * Write service document based on given {@link Edm} and <code>service root</code> as
   * content type "<code>application/atomsvc+xml; charset=utf-8</code>".
   * 
   * @param edm the Entity Data Model
   * @param serviceRoot the root URI of the service
   * @return resulting {@link ODataResponse} with written service document
   * @throws EntityProviderException
   */
  public abstract ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException;

  /**
   * Write property as binary or as content type <code>text/plain</code>.
   * @param edmProperty the EDM property
   * @param value its value
   * @return resulting {@link ODataResponse} with written content
   * @throws EntityProviderException
   */
  public abstract ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws EntityProviderException;

  /**
   * Write text value as content type <code>text/plain</code>.
   * @param value the string that is written to {@link ODataResponse}
   * @return resulting {@link ODataResponse} with written text content
   * @throws EntityProviderException
   */
  public abstract ODataResponse writeText(String value) throws EntityProviderException;

  /**
   * Write binary content with content type header set to given <code>mime type</code> parameter.
   * @param mimeType MIME type which is written and used as content type header information
   * @param data data is written to {@link ODataResponse}
   * @return resulting {@link ODataResponse} with written binary content
   * @throws EntityProviderException
   */
  public abstract ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException;

  /**
   * Reads an unformatted value of an EDM property as binary or as content type <code>text/plain</code>.
   * @param edmProperty the EDM property
   * @param content the content input stream
   * @return the value as the proper system data type
   * @throws EntityProviderException
   */
  public abstract Object readPropertyValue(EdmProperty edmProperty, InputStream content) throws EntityProviderException;

  /**
   * Reads binary data from an input stream.
   * @param content the content input stream
   * @return the binary data
   * @throws EntityProviderException
   */
  public abstract byte[] readBinary(InputStream content) throws EntityProviderException;
}
