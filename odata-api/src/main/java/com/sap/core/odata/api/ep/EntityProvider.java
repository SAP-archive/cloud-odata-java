package com.sap.core.odata.api.ep;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * static provider for writing output
 * @author SAP AG
 */
public final class EntityProvider {

  /**
   * 
   */
  public interface EntityProviderInterface {

    /**
     * Write service document based on given {@link Edm} and <code>service root</code> as
     * given content type.
     * 
     * @param contentType
     * @param edm
     * @param serviceRoot
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeServiceDocument(String contentType, Edm edm, String serviceRoot) throws EntityProviderException;

    /**
     * Write property as content type <code>application/octet-stream</code> or <code>text/plain</code>.
     * 
     * @param edmProperty
     * @param value
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws EntityProviderException;

    /**
     * Write text value as content type <code>text/plain</code>.
     * 
     * @param value
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeText(String value) throws EntityProviderException;

    /**
     * Write binary content with content type header set to given <code>mime type</code> parameter.
     * 
     * @param mimeType mime type which is written and used as content type header information.
     * @param data which is written to {@link ODataResponse}.
     * @return resulting {@link ODataResponse} with written binary content.
     * @throws EntityProviderException
     */
    ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param entitySet
     * @param data
     * @param properties
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeFeed(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param entitySet
     * @param data
     * @param properties
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeEntry(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param edmProperty
     * @param value
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeProperty(String contentType, EdmProperty edmProperty, Object value) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param entitySet
     * @param data
     * @param properties
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeLink(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param entitySet
     * @param data
     * @param properties
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeLinks(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param functionImport
     * @param data
     * @param properties
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeFunctionImport(String contentType, EdmFunctionImport functionImport, Object data, EntityProviderProperties properties) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param entitySet
     * @param content
     * @return
     * @throws EntityProviderException
     */
    ODataEntry readEntry(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param edmProperty
     * @param content
     * @return
     * @throws EntityProviderException
     */
    Map<String, Object> readProperty(String contentType, EdmProperty edmProperty, InputStream content) throws EntityProviderException;

    /**
     * 
     * @param edmProperty
     * @param content
     * @return
     * @throws EntityProviderException
     */
    Object readPropertyValue(EdmProperty edmProperty, InputStream content) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param entitySet
     * @param content
     * @return
     * @throws EntityProviderException
     */
    List<String> readLinks(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    /**
     * 
     * @param contentType
     * @param entitySet
     * @param content
     * @return
     * @throws EntityProviderException
     */
    String readLink(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    /**
     * 
     * @param content
     * @return
     * @throws EntityProviderException
     */
    byte[] readBinary(InputStream content) throws EntityProviderException;
 }

  /**
   * Create an instance for the {@link EntityProviderInterface} over the {@link RuntimeDelegate}.
   * 
   * @return instance of {@link EntityProviderInterface}
   */
  private static EntityProviderInterface createEntityProvider() {
    return RuntimeDelegate.createEntityProvider();
  }

  /**
   * Write service document based on given {@link Edm} and <code>service root</code> as
   * given content type.
   * 
   * @param edm
   * @param serviceRoot
   * @return
   * @throws EntityProviderException
   */
  public static ODataResponse writeServiceDocument(String contentType, Edm edm, String serviceRoot) throws EntityProviderException {
    return createEntityProvider().writeServiceDocument(contentType, edm, serviceRoot);
  }

  /**
   * Write property as content type <code>application/octet-stream</code> or <code>text/plain</code>.
   * 
   * @param edmProperty
   * @param value
   * @return
   * @throws EntityProviderException
   */
  public static ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws EntityProviderException {
    return createEntityProvider().writePropertyValue(edmProperty, value);
  }

  /**
   * Write text value as content type <code>text/plain</code>.
   * 
   * @param value
   * @return
   * @throws EntityProviderException
   */
  public static ODataResponse writeText(String value) throws EntityProviderException {
    return createEntityProvider().writeText(value);
  }

  /**
   * Write binary content with content type header set to given <code>mime type</code> parameter.
   * 
   * @param mimeType mime type which is written and used as content type header information.
   * @param data which is written to {@link ODataResponse}.
   * @return resulting {@link ODataResponse} with written binary content.
   * @throws EntityProviderException
   */
  public static ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException {
    return createEntityProvider().writeBinary(mimeType, data);
  }

  public static ODataResponse writeFeed(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException {
    return createEntityProvider().writeFeed(contentType, entitySet, data, properties);
  }

  public static ODataResponse writeEntry(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException {
    return createEntityProvider().writeEntry(contentType, entitySet, data, properties);
  }

  public static ODataResponse writeProperty(String contentType, EdmProperty edmProperty, Object value) throws EntityProviderException {
    return createEntityProvider().writeProperty(contentType, edmProperty, value);
  }

  public static ODataResponse writeLink(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException {
    return createEntityProvider().writeLink(contentType, entitySet, data, properties);
  }

  public static ODataResponse writeLinks(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException {
    return createEntityProvider().writeLinks(contentType, entitySet, data, properties);
  }

  public static ODataResponse writeFunctionImport(String contentType, EdmFunctionImport functionImport, Object data, EntityProviderProperties properties) throws EntityProviderException {
    return createEntityProvider().writeFunctionImport(contentType, functionImport, data, properties);
  }

  public static ODataEntry readEntry(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException {
    return createEntityProvider().readEntry(contentType, entitySet, content);
  }

  public static Map<String, Object> readProperty(String contentType, EdmProperty edmProperty, InputStream content) throws EntityProviderException {
    return createEntityProvider().readProperty(contentType, edmProperty, content);
  }

  public static Object readPropertyValue(EdmProperty edmProperty, InputStream content) throws EntityProviderException {
    return createEntityProvider().readPropertyValue(edmProperty, content);
  }

  public static List<String> readLinks(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException {
    return createEntityProvider().readLinks(contentType, entitySet, content);
  }

  public static String readLink(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException {
    return createEntityProvider().readLink(contentType, entitySet, content);
  }

  public static byte[] readBinary(InputStream content) throws EntityProviderException {
    return createEntityProvider().readBinary(content);
  }
}
