package com.sap.core.odata.api.ep;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * static provider for writing output
 * @author SAP AG
 */
public final class EntityProvider {

  public interface ProviderInterface {

    /**
     * Write service document based on given {@link Edm} and <code>service root</code> as
     * content type "<code>application/atomsvc+xml; charset=utf-8</code>".
     * 
     * @param edm
     * @param serviceRoot
     * @return
     * @throws EntityProviderException
     */
    ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException;

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

    ODataResponse writeFeed(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException;

    ODataResponse writeEntry(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException;

    ODataResponse writeProperty(String contentType, EdmProperty edmProperty, Object value) throws EntityProviderException;

    ODataResponse writeLink(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException;

    ODataResponse writeLinks(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException;

    ODataResponse writeFunctionImport(String contentType, EdmFunctionImport functionImport, Object data, EntityProviderProperties properties) throws EntityProviderException;

    
    ReadEntryResult readEntry(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    Map<String, Object> readProperty(String contentType, EdmProperty edmProperty, InputStream content) throws EntityProviderException;

    Object readPropertyValue(EdmProperty edmProperty, InputStream content) throws EntityProviderException;

    List<String> readLinks(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    String readLink(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    byte[] readBinary(InputStream content) throws EntityProviderException;
  }

  
  private static ProviderInterface createBasic() {
    return RuntimeDelegate.createProviderFacade();
  }  

  /**
   * Write service document based on given {@link Edm} and <code>service root</code> as
   * content type "<code>application/atomsvc+xml; charset=utf-8</code>".
   * 
   * @param edm
   * @param serviceRoot
   * @return
   * @throws EntityProviderException
   */
  public static ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException{
    return createBasic().writeServiceDocument(edm, serviceRoot);
  };

  /**
   * Write property as content type <code>application/octet-stream</code> or <code>text/plain</code>.
   * 
   * @param edmProperty
   * @param value
   * @return
   * @throws EntityProviderException
   */
  public static ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws EntityProviderException{
    return createBasic().writePropertyValue(edmProperty, value);
  };

  /**
   * Write text value as content type <code>text/plain</code>.
   * 
   * @param value
   * @return
   * @throws EntityProviderException
   */
  public static ODataResponse writeText(String value) throws EntityProviderException{
    return createBasic().writeText(value);
  };

  /**
   * Write binary content with content type header set to given <code>mime type</code> parameter.
   * 
   * @param mimeType mime type which is written and used as content type header information.
   * @param data which is written to {@link ODataResponse}.
   * @return resulting {@link ODataResponse} with written binary content.
   * @throws EntityProviderException
   */
  public static ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException{
    return createBasic().writeBinary(mimeType, data);
  };

  public static ODataResponse writeFeed(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException{
    return createBasic().writeFeed(contentType, entitySet, data, properties);
  };

  public static ODataResponse writeEntry(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException{
    return createBasic().writeEntry(contentType, entitySet, data, properties);
  };

  public static ODataResponse writeProperty(String contentType, EdmProperty edmProperty, Object value) throws EntityProviderException{
    return createBasic().writeProperty(contentType, edmProperty, value);
  };

  public static ODataResponse writeLink(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException{
    return createBasic().writeLink(contentType, entitySet, data, properties);
  };

  public static ODataResponse writeLinks(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException{
    return createBasic().writeLinks(contentType, entitySet, data, properties);
  };

  public static ODataResponse writeFunctionImport(String contentType, EdmFunctionImport functionImport, Object data, EntityProviderProperties properties) throws EntityProviderException{
    return createBasic().writeFunctionImport(contentType, functionImport, data, properties);
  };

  public static ReadEntryResult readEntry(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException{
    return createBasic().readEntry(contentType, entitySet, content);
  };

  public static Map<String, Object> readProperty(String contentType, EdmProperty edmProperty, InputStream content) throws EntityProviderException{
    return createBasic().readProperty(contentType, edmProperty, content);
  };

  public static Object readPropertyValue(EdmProperty edmProperty, InputStream content) throws EntityProviderException {
    return createBasic().readPropertyValue(edmProperty, content);
  }

  public static List<String> readLinks(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException{
    return createBasic().readLinks(contentType, entitySet, content);
  };

  public static String readLink(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException {
    return createBasic().readLink(contentType, entitySet, content);
  }

  public static byte[] readBinary(InputStream content) throws EntityProviderException {
    return createBasic().readBinary(content);
  }
}
