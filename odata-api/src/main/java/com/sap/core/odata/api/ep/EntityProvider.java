package com.sap.core.odata.api.ep;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.servicedocument.ServiceDocument;

/**
 * <p>Entity Provider</p> 
 * <p>An {@link EntityProvider} provides all necessary <b>read</b> and <b>write</b>
 * methods for accessing the entities defined in an <code>Entity Data Model</code>.
 * Therefore this library provides (in its <code>core</code> packages) as convenience
 * basic entity providers for accessing entities in the <b>XML</b> and <b>JSON</b>
 * formats.</p>
 * @author SAP AG
 */
public final class EntityProvider {

  /**
   * (Internal) interface for all {@link EntityProvider} necessary <b>read</b> and <b>write</b> methods for accessing 
   * entities defined in an <code>Entity Data Model</code>.
   * <p>
   * This interface is declared as inner interface (class) because the {@link EntityProvider} provides a convenience
   * access (and basic implementation for <b>XML</b> and <b>JSON</b> format) to all interface methods.
   * <br/>
   * Hence, it is <b>not recommended</b> to implement this interface (it is possible to implement it and to provide an 
   * own {@link EntityProvider} for support of additional formats but it is recommended to
   * handle additional formats directly within an <code>ODataProcessor</code>).
   */
  public interface EntityProviderInterface {

    /**
     * Write metadata document in XML format for the given schemas and the provided predefined 
     * namespaces at the EDMX element. PredefinedNamespaces is of type 
     * Map{@literal <}prefix,namespace{@literal >} and may be null or an empty Map.
     * 
     * @param schemas all XML schemas which will be written
     * @param predefinedNamespaces type of Map{@literal <}prefix,namespace{@literal >} and may be null or an empty Map
     * @return resulting {@link ODataResponse} with written metadata content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeMetadata(List<Schema> schemas, Map<String, String> predefinedNamespaces) throws EntityProviderException;

    /**
     * Write service document based on given {@link Edm} and <code>service root</code> as
     * given content type.
     * 
     * @param contentType format in which service document should be written
     * @param edm entity data model to be written
     * @param serviceRoot service root for the written service document
     * @return resulting {@link ODataResponse} with written service document content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeServiceDocument(String contentType, Edm edm, String serviceRoot) throws EntityProviderException;

    /**
     * Write property as content type <code>application/octet-stream</code> or <code>text/plain</code>.
     * 
     * @param edmProperty entity data model for to be written property
     * @param value property which will be written
     * @return resulting {@link ODataResponse} with written property value content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws EntityProviderException;

    /**
     * Write text value as content type <code>text/plain</code>.
     * 
     * @param value text value which will be written
     * @return resulting {@link ODataResponse} with written text/plain content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeText(String value) throws EntityProviderException;

    /**
     * Write binary content with content type header set to given <code>mime type</code> parameter.
     * 
     * @param mimeType mime type which is written and used as content type header information.
     * @param data which is written to {@link ODataResponse}.
     * @return response object resulting {@link ODataResponse} with written binary content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException;

    /**
     * Write given <code>data</code> (which is given in form of a {@link List} with a {@link Map} for each entity. Such a {@link Map}
     * contains all properties [as <code>property name</code> to <code>property value</code> mapping] for the entry) in the specified
     * format (given as <code>contentType</code>) based on given <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
     * and <code>properties</code> for this entity provider (given as {@link EntityProviderWriteProperties}).
     * 
     * @param contentType format in which the feed should be written
     * @param entitySet entity data model for given entity data set
     * @param data set of entries in form of a {@link List} with a {@link Map} for each entity (such a {@link Map}
     *              contains all properties [as <code>property name</code> to <code>property value</code> mapping).
     * @param properties additional properties necessary for writing of data 
     * @return resulting {@link ODataResponse} with written feed content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeFeed(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderWriteProperties properties) throws EntityProviderException;

    /**
     * Write given <code>data</code> (which is given in form of a {@link Map} for which contains all properties 
     * as <code>property name</code> to <code>property value</code> mapping) for the entry in the specified
     * format (given as <code>contentType</code>) based on <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
     * and <code>properties</code> for this entity provider (given as {@link EntityProviderWriteProperties}).
     * 
     * @param contentType format in which the entry should be written
     * @param entitySet entity data model for given entity data set
     * @param data which contains all properties as <code>property name</code> to <code>property value</code> mapping for the entry
     * @param properties additional properties necessary for writing of data 
     * @return resulting {@link ODataResponse} with written entry content
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeEntry(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderWriteProperties properties) throws EntityProviderException;

    /**
     * Write given <code>value</code> (which is given in form of an {@link Object}) for the property in the specified
     * format (given as <code>contentType</code>) based on given <code>entity data model for an entity property</code> 
     * (given as {@link EdmProperty}).
     * 
     * @param contentType format in which the property should be written
     * @param edmProperty entity data model for given property
     * @param value data which is written
     * @return resulting {@link ODataResponse} with written property content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeProperty(String contentType, EdmProperty edmProperty, Object value) throws EntityProviderException;

    /**
     * Write <b>link</b> for key property based on <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
     * in the specified format (given as <code>contentType</code>).
     * The necessary key property values must be provided within the <code>data</code> (in the form of <code>property name</code>
     * to <code>property value</code> mapping) and <code>properties</code> for this entity provider must be set
     * (given as {@link EntityProviderWriteProperties}).
     * 
     * @param contentType format in which the entry should be written
     * @param entitySet entity data model for given entity data set
     * @param data which contains all key properties as <code>property name</code> to <code>property value</code> mapping for the entry
     * @param properties additional properties necessary for writing of data 
     * @return resulting {@link ODataResponse} with written link content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeLink(String contentType, EdmEntitySet entitySet, Map<String, Object> data, EntityProviderWriteProperties properties) throws EntityProviderException;

    /**
     * Write all <b>links</b> for key property based on <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
     * in the specified format (given as <code>contentType</code>) for a set of entries.
     * The necessary key property values must be provided within the <code>data</code> (in form of a {@link List} with a {@link Map} 
     * for each entry. Such a {@link Map} contains all key properties [as <code>property name</code> to 
     * <code>property value</code> mapping] for the entry) and <code>properties</code> for this entity provider must be set
     * (given as {@link EntityProviderWriteProperties}).
     * 
     * @param contentType format in which the entry should be written
     * @param entitySet entity data model for given entity data set
     * @param data set of entries in form of a {@link List} with a {@link Map} for each entry (such a {@link Map}
     *              contains all key properties [as <code>property name</code> to <code>property value</code> mapping).
     * @param properties additional properties necessary for writing of data 
     * @return resulting {@link ODataResponse} with written links content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeLinks(String contentType, EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderWriteProperties properties) throws EntityProviderException;

    /**
     * Write <code>data</code> result (given as {@link Object}) of function import based on <code>return type</code> 
     * of {@link EdmFunctionImport} in specified format (given as <code>contentType</code>). Additional <code>properties</code> 
     * for this entity provider must be set (given as {@link EntityProviderWriteProperties}).
     * 
     * @param contentType format in which the entry should be written
     * @param functionImport entity data model for executed function import
     * @param data result of function import
     * @param properties additional properties necessary for writing of data 
     * @return resulting {@link ODataResponse} with written function import result content.
     * @throws EntityProviderException if writing of data (serialization) fails
     */
    ODataResponse writeFunctionImport(String contentType, EdmFunctionImport functionImport, Object data, EntityProviderWriteProperties properties) throws EntityProviderException;

    /**
     * Read (de-serialize) a data feed from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
     * based on <code>entity data model</code> (given as {@link EdmEntitySet}) and provide this data as {@link ODataEntry}.
     * 
     * @param contentType format of content in the given input stream.
     * @param entitySet entity data model for entity set to be read
     * @param content feed data in form of an {@link InputStream} which contains the data in specified format
     * @param properties additional properties necessary for reading content from {@link InputStream} into {@link Map}.
     * @return an {@link ODataFeed} object
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    ODataFeed readFeed(String contentType, EdmEntitySet entitySet, InputStream content, EntityProviderReadProperties properties) throws EntityProviderException;

    /**
     * Read (de-serialize) data from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
     * based on <code>entity data model</code> (given as {@link EdmEntitySet}) and provide this data as {@link ODataEntry}.
     * 
     * @param contentType format of content in the given input stream.
     * @param entitySet entity data model for entity set to be read
     * @param content data in form of an {@link InputStream} which contains the data in specified format
     * @param properties additional properties necessary for reading content from {@link InputStream} into {@link Map}.
     * @return entry as {@link ODataEntry}
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    ODataEntry readEntry(String contentType, EdmEntitySet entitySet, InputStream content, EntityProviderReadProperties properties) throws EntityProviderException;

    /**
     * Read (de-serialize) properties from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
     * based on <code>entity data model</code> (given as {@link EdmProperty}) and provide this data as {@link Map} which contains
     * the read data in form of <code>property name</code> to <code>property value</code> mapping.
     * 
     * @param contentType format of content in the given input stream.
     * @param edmProperty entity data model for entity property to be read
     * @param content data in form of an {@link InputStream} which contains the data in specified format
     * @param properties additional properties necessary for reading content from {@link InputStream} into {@link Map}.
     * @return property as name and value in a map
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    Map<String, Object> readProperty(String contentType, EdmProperty edmProperty, InputStream content, EntityProviderReadProperties properties) throws EntityProviderException;

    /**
     * Read (de-serialize) a property value from <code>content</code> (as {@link InputStream}) in format <code>text/plain</code>
     * based on <code>entity data model</code> (given as {@link EdmProperty}) and provide this data as {@link Object}.
     * 
     * @param edmProperty entity data model for entity property to be read
     * @param content data in form of an {@link InputStream} which contains the data in format <code>text/plain</code>
     * @param typeMapping defines the mapping for this <code>edm property</code> to a <code>java class</code> which should be used
     *                  during read of the content. If according <code>edm property</code> can not be read
     *                  into given <code>java class</code> an {@link EntityProviderException} is thrown.
     *                  Supported mappings are documented in {@link com.sap.core.odata.api.edm.EdmSimpleType}.
     * @return property value as object
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    Object readPropertyValue(EdmProperty edmProperty, InputStream content, Class<?> typeMapping) throws EntityProviderException;

    /**
     * Read (de-serialize) a link from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
     * based on <code>entity data model</code> (given as {@link EdmEntitySet}) and provide the link as {@link String}.
     * 
     * @param contentType format of content in the given input stream.
     * @param entitySet entity data model for entity property to be read
     * @param content data in form of an {@link InputStream} which contains the data in specified format
     * @return link as string
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    String readLink(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    /**
     * Read (de-serialize) all links from <code>content</code> (as {@link InputStream})
     * in specified format (given as <code>contentType</code>) based on <code>entity data model</code>
     * (given as {@link EdmEntitySet}) and provide the link as List of Strings.
     *
     * @param contentType format of content in the given input stream.
     * @param entitySet entity data model for entity property to be read
     * @param content data in form of an {@link InputStream} which contains the data in specified format
     * @return links as List of Strings
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    List<String> readLinks(String contentType, EdmEntitySet entitySet, InputStream content) throws EntityProviderException;

    /**
     * Read (de-serialize) data from metadata <code>inputStream</code> (as {@link InputStream}) and provide Edm as {@link Edm}
     * 
     * @param inputStream the given input stream
     * @param validate has to be true if metadata should be validated 
     * @return Edm as {@link Edm}
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    Edm readMetadata(InputStream inputStream, boolean validate) throws EntityProviderException;

    /**
     * Read (de-serialize) binary data from <code>content</code> (as {@link InputStream}) and provide it as <code>byte[]</code>.
     * 
     * @param content data in form of an {@link InputStream} which contains the binary data
     * @return binary data as bytes
     * @throws EntityProviderException if reading of data (de-serialization) fails
     */
    byte[] readBinary(InputStream content) throws EntityProviderException;

    /**
     * <p>Serializes an error message according to the OData standard.</p>
     * @param context     contains error details see {@link ODataErrorContext}
     * @return            an {@link ODataResponse} containing the serialized error message
     */
    ODataResponse writeErrorDocument(ODataErrorContext context);

    /**
     * Read (de-serialize) data from service document <code>inputStream</code> (as {@link InputStream}) and provide ServiceDocument as {@link ServiceDocument}
     * 
     * @param serviceDocument the given input stream
     * @param contentType format of content in the given input stream
     * @return ServiceDocument as {@link ServiceDocument}
     * @throws EntityProviderException  if reading of data (de-serialization) fails
     */
    ServiceDocument readServiceDocument(InputStream serviceDocument, String contentType) throws EntityProviderException;

    /**
     * Parse Batch Request body <code>inputStream</code> (as {@link InputStream}) and provide a list of Batch Parts as {@link BatchPart}
     * 
     * @param contentType format of content in the given input stream
     * @param content request body
     * @param properties additional properties necessary for parsing. Must not be null.
     * @return list of {@link BatchPart}
     * @throws EntityProviderException  if parsing fails
     */
    List<BatchPart> parseBatchRequest(String contentType, InputStream content, EntityProviderBatchProperties properties) throws EntityProviderException;

    /**
     * Write responses of Batch Response Parts in Batch Response as {@link ODataResponse}.
     * Batch Response body matches one-to-one with the corresponding Batch Request body
     * 
     * @param batchResponseParts a list of {@link BatchResponsePart}
     * @return Batch Response as {@link ODataResponse}
     * @throws EntityProviderException 
     */
    ODataResponse writeBatchResponse(List<BatchResponsePart> batchResponseParts) throws EntityProviderException;
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
   * <p>Serializes an error message according to the OData standard.</p>
   * An exception is not thrown because this method is used in exception handling.</p>
   * @param context     contains error details see {@link ODataErrorContext}
   * @return            an {@link ODataResponse} containing the serialized error message
   */
  public static ODataResponse writeErrorDocument(final ODataErrorContext context) {
    return createEntityProvider().writeErrorDocument(context);
  }

  /**
   * Write metadata document in XML format for the given schemas and the provided predefined 
   * namespaces at the EDMX element. PredefinedNamespaces is of type 
   * Map{@literal <}prefix,namespace{@literal >} and may be null or an empty Map.
   * 
   * @param schemas all XML schemas which will be written
   * @param predefinedNamespaces type of Map{@literal <}prefix,namespace{@literal >} and may be null or an empty Map
   * @return resulting {@link ODataResponse} with written metadata content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeMetadata(final List<Schema> schemas, final Map<String, String> predefinedNamespaces) throws EntityProviderException {
    return createEntityProvider().writeMetadata(schemas, predefinedNamespaces);
  }

  /**
   * Write service document based on given {@link Edm} and <code>service root</code> as
   * given content type.
   * 
   * @param contentType format in which service document should be written
   * @param edm entity data model to be written
   * @param serviceRoot service root for the written service document
   * @return resulting {@link ODataResponse} with written service document content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeServiceDocument(final String contentType, final Edm edm, final String serviceRoot) throws EntityProviderException {
    return createEntityProvider().writeServiceDocument(contentType, edm, serviceRoot);
  }

  /**
   * Write property as content type <code>application/octet-stream</code> or <code>text/plain</code>.
   * 
   * @param edmProperty entity data model for to be written property
   * @param value property which will be written
   * @return resulting {@link ODataResponse} with written property value content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writePropertyValue(final EdmProperty edmProperty, final Object value) throws EntityProviderException {
    return createEntityProvider().writePropertyValue(edmProperty, value);
  }

  /**
   * Write text value as content type <code>text/plain</code>.
   * 
   * @param value text value which will be written
   * @return resulting {@link ODataResponse} with written text/plain content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeText(final String value) throws EntityProviderException {
    return createEntityProvider().writeText(value);
  }

  /**
   * Write binary content with content type header set to given <code>mime type</code> parameter.
   * 
   * @param mimeType mime type which is written and used as content type header information.
   * @param data which is written to {@link ODataResponse}.
   * @return response object resulting {@link ODataResponse} with written binary content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeBinary(final String mimeType, final byte[] data) throws EntityProviderException {
    return createEntityProvider().writeBinary(mimeType, data);
  }

  /**
   * Write given <code>data</code> (which is given in form of a {@link List} with a {@link Map} for each entity. Such a {@link Map}
   * contains all properties [as <code>property name</code> to <code>property value</code> mapping] for the entry) in the specified
   * format (given as <code>contentType</code>) based on given <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
   * and <code>properties</code> for this entity provider (given as {@link EntityProviderWriteProperties}).
   * 
   * @param contentType format in which the feed should be written
   * @param entitySet entity data model for given entity data set
   * @param data set of entries in form of a {@link List} with a {@link Map} for each entity (such a {@link Map}
   *              contains all properties [as <code>property name</code> to <code>property value</code> mapping).
   * @param properties additional properties necessary for writing of data 
   * @return resulting {@link ODataResponse} with written feed content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeFeed(final String contentType, final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    return createEntityProvider().writeFeed(contentType, entitySet, data, properties);
  }

  /**
   * Write given <code>data</code> (which is given in form of a {@link Map} for which contains all properties 
   * as <code>property name</code> to <code>property value</code> mapping) for the entry in the specified
   * format (given as <code>contentType</code>) based on <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
   * and <code>properties</code> for this entity provider (given as {@link EntityProviderWriteProperties}).
   * 
   * @param contentType format in which the entry should be written
   * @param entitySet entity data model for given entity data set
   * @param data which contains all properties as <code>property name</code> to <code>property value</code> mapping for the entry
   * @param properties additional properties necessary for writing of data 
   * @return resulting {@link ODataResponse} with written entry content
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeEntry(final String contentType, final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    return createEntityProvider().writeEntry(contentType, entitySet, data, properties);
  }

  /**
   * Write given <code>value</code> (which is given in form of an {@link Object}) for the property in the specified
   * format (given as <code>contentType</code>) based on given <code>entity data model for an entity property</code> 
   * (given as {@link EdmProperty}).
   * 
   * @param contentType format in which the property should be written
   * @param edmProperty entity data model for given property
   * @param value data which is written
   * @return resulting {@link ODataResponse} with written property content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeProperty(final String contentType, final EdmProperty edmProperty, final Object value) throws EntityProviderException {
    return createEntityProvider().writeProperty(contentType, edmProperty, value);
  }

  /**
   * Write <b>link</b> for key property based on <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
   * in the specified format (given as <code>contentType</code>).
   * The necessary key property values must be provided within the <code>data</code> (in the form of <code>property name</code>
   * to <code>property value</code> mapping) and <code>properties</code> for this entity provider must be set
   * (given as {@link EntityProviderWriteProperties}).
   * 
   * @param contentType format in which the entry should be written
   * @param entitySet entity data model for given entity data set
   * @param data which contains all key properties as <code>property name</code> to <code>property value</code> mapping for the entry
   * @param properties additional properties necessary for writing of data 
   * @return resulting {@link ODataResponse} with written link content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeLink(final String contentType, final EdmEntitySet entitySet, final Map<String, Object> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    return createEntityProvider().writeLink(contentType, entitySet, data, properties);
  }

  /**
   * Write all <b>links</b> for key property based on <code>entity data model for an entity set</code> (given as {@link EdmEntitySet})
   * in the specified format (given as <code>contentType</code>) for a set of entries.
   * The necessary key property values must be provided within the <code>data</code> (in form of a {@link List} with a {@link Map} 
   * for each entry. Such a {@link Map} contains all key properties [as <code>property name</code> to 
   * <code>property value</code> mapping] for the entry) and <code>properties</code> for this entity provider must be set
   * (given as {@link EntityProviderWriteProperties}).
   * 
   * @param contentType format in which the entry should be written
   * @param entitySet entity data model for given entity data set
   * @param data set of entries in form of a {@link List} with a {@link Map} for each entry (such a {@link Map}
   *              contains all key properties [as <code>property name</code> to <code>property value</code> mapping).
   * @param properties additional properties necessary for writing of data 
   * @return resulting {@link ODataResponse} with written links content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeLinks(final String contentType, final EdmEntitySet entitySet, final List<Map<String, Object>> data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    return createEntityProvider().writeLinks(contentType, entitySet, data, properties);
  }

  /**
   * Write <code>data</code> result (given as {@link Object}) of function import based on <code>return type</code> 
   * of {@link EdmFunctionImport} in specified format (given as <code>contentType</code>). Additional <code>properties</code> 
   * for this entity provider must be set (given as {@link EntityProviderWriteProperties}).
   * 
   * @param contentType format in which the entry should be written
   * @param functionImport entity data model for executed function import
   * @param data result of function import
   * @param properties additional properties necessary for writing of data 
   * @return resulting {@link ODataResponse} with written function import result content.
   * @throws EntityProviderException if writing of data (serialization) fails
   */
  public static ODataResponse writeFunctionImport(final String contentType, final EdmFunctionImport functionImport, final Object data, final EntityProviderWriteProperties properties) throws EntityProviderException {
    return createEntityProvider().writeFunctionImport(contentType, functionImport, data, properties);
  }

  /**
   * Read (de-serialize) a data feed from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
   * based on <code>entity data model</code> (given as {@link EdmEntitySet}) and provide this data as {@link ODataEntry}.
   * 
   * @param contentType format of content in the given input stream.
   * @param entitySet entity data model for entity set to be read
   * @param content feed data in form of an {@link InputStream} which contains the data in specified format
   * @param properties additional properties necessary for reading content from {@link InputStream} into {@link Map}. Must not be null.
   * @return an {@link ODataFeed} object
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static ODataFeed readFeed(final String contentType, final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    return createEntityProvider().readFeed(contentType, entitySet, content, properties);
  }

  /**
   * Read (de-serialize) data from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
   * based on <code>entity data model</code> (given as {@link EdmEntitySet}) and provide this data as {@link ODataEntry}.
   * 
   * @param contentType format of content in the given input stream.
   * @param entitySet entity data model for entity set to be read
   * @param content data in form of an {@link InputStream} which contains the data in specified format
   * @param properties additional properties necessary for reading content from {@link InputStream} into {@link Map}. Must not be null.
   * @return entry as {@link ODataEntry}
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static ODataEntry readEntry(final String contentType, final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    return createEntityProvider().readEntry(contentType, entitySet, content, properties);
  }

  /**
   * Read (de-serialize) properties from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
   * based on <code>entity data model</code> (given as {@link EdmProperty}) and provide this data as {@link Map} which contains
   * the read data in form of <code>property name</code> to <code>property value</code> mapping.
   * 
   * @param contentType format of content in the given input stream.
   * @param edmProperty entity data model for entity property to be read
   * @param content data in form of an {@link InputStream} which contains the data in specified format
   * @param properties additional properties necessary for reading content from {@link InputStream} into {@link Map}. Must not be null.
   * @return property as name and value in a map
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static Map<String, Object> readProperty(final String contentType, final EdmProperty edmProperty, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    return createEntityProvider().readProperty(contentType, edmProperty, content, properties);
  }

  /**
   * Read (de-serialize) a property value from <code>content</code> (as {@link InputStream}) in format <code>text/plain</code>
   * based on <code>entity data model</code> (given as {@link EdmProperty}) and provide this data as {@link Object}.
   * 
   * @param edmProperty entity data model for entity property to be read
   * @param content data in form of an {@link InputStream} which contains the data in format <code>text/plain</code>
   * @return property value as object
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static Object readPropertyValue(final EdmProperty edmProperty, final InputStream content) throws EntityProviderException {
    return createEntityProvider().readPropertyValue(edmProperty, content, null);
  }

  /**
   * Read (de-serialize) a property value from <code>content</code> (as {@link InputStream}) in format <code>text/plain</code>
   * based on <code>entity data model</code> (given as {@link EdmProperty}) and provide this data as {@link Object}.
   * 
   * @param edmProperty entity data model for entity property to be read
   * @param content data in form of an {@link InputStream} which contains the data in format <code>text/plain</code>
   * @param typeMapping defines the mapping for this <code>edm property</code> to a <code>java class</code> which should be used
   *                  during read of the content. If according <code>edm property</code> can not be read
   *                  into given <code>java class</code> an {@link EntityProviderException} is thrown.
   *                  Supported mappings are documented in {@link com.sap.core.odata.api.edm.EdmSimpleType}.
   * @return property value as object
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static Object readPropertyValue(final EdmProperty edmProperty, final InputStream content, final Class<?> typeMapping) throws EntityProviderException {
    return createEntityProvider().readPropertyValue(edmProperty, content, typeMapping);
  }

  /**
   * Read (de-serialize) a link from <code>content</code> (as {@link InputStream}) in specified format (given as <code>contentType</code>)
   * based on <code>entity data model</code> (given as {@link EdmEntitySet}) and provide the link as {@link String}.
   * 
   * @param contentType format of content in the given input stream.
   * @param entitySet entity data model for entity property to be read
   * @param content data in form of an {@link InputStream} which contains the data in specified format
   * @return link as string
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static String readLink(final String contentType, final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    return createEntityProvider().readLink(contentType, entitySet, content);
  }

  /**
   * Read (de-serialize) a link collection from <code>content</code> (as {@link InputStream})
   * in specified format (given as <code>contentType</code>) based on <code>entity data model</code>
   * (given as {@link EdmEntitySet}) and provide the links as List of Strings.
   *
   * @param contentType format of content in the given input stream.
   * @param entitySet entity data model for entity property to be read
   * @param content data in form of an {@link InputStream} which contains the data in specified format
   * @return links as List of Strings
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static List<String> readLinks(final String contentType, final EdmEntitySet entitySet, final InputStream content) throws EntityProviderException {
    return createEntityProvider().readLinks(contentType, entitySet, content);
  }

  /**
   * Read (de-serialize) binary data from <code>content</code> (as {@link InputStream}) and provide it as <code>byte[]</code>.
   * 
   * @param content data in form of an {@link InputStream} which contains the binary data
   * @return binary data as bytes
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static byte[] readBinary(final InputStream content) throws EntityProviderException {
    return createEntityProvider().readBinary(content);
  }

  /**
   * Read (de-serialize) data from metadata <code>inputStream</code> (as {@link InputStream}) and provide Edm as {@link Edm}
   * 
   * @param metadataXml a metadata xml input stream (means the metadata document)
   * @param validate has to be true if metadata should be validated 
   * @return Edm as {@link Edm}
   * @throws EntityProviderException if reading of data (de-serialization) fails
   */
  public static Edm readMetadata(final InputStream metadataXml, final boolean validate) throws EntityProviderException {
    return createEntityProvider().readMetadata(metadataXml, validate);
  }

  /**
   * Read (de-serialize) data from service document <code>inputStream</code> (as {@link InputStream}) and provide ServiceDocument as {@link ServiceDocument}
   * 
   * @param serviceDocument the given input stream
   * @param contentType format of content in the given input stream
   * @return ServiceDocument as {@link ServiceDocument}
   * @throws EntityProviderException  if reading of data (de-serialization) fails
   */
  public static ServiceDocument readServiceDocument(final InputStream serviceDocument, final String contentType) throws EntityProviderException {
    return createEntityProvider().readServiceDocument(serviceDocument, contentType);
  }

  /**
   * Parse Batch Request body <code>inputStream</code> (as {@link InputStream}) and provide a list of Batch Parts as {@link BatchPart}
   * 
   * @param contentType format of content in the given input stream
   * @param content request body
   * @param properties additional properties necessary for parsing. Must not be null.
   * @return list of {@link BatchPart}
   * @throws EntityProviderException  if parsing fails
   */
  public static List<BatchPart> parseBatchRequest(final String contentType, final InputStream content, final EntityProviderBatchProperties properties) throws EntityProviderException {
    return createEntityProvider().parseBatchRequest(contentType, content, properties);
  }

  /**
   * Write responses of Batch Response Parts in Batch Response as {@link ODataResponse}.
   * Batch Response body matches one-to-one with the corresponding Batch Request body
   * 
   * @param batchResponseParts a list of {@link BatchResponsePart}
   * @return Batch Response as {@link ODataResponse}
   * @throws EntityProviderException 
   */
  public static ODataResponse writeBatchResponse(final List<BatchResponsePart> batchResponseParts) throws EntityProviderException {
    return createEntityProvider().writeBatchResponse(batchResponseParts);
  }

}
