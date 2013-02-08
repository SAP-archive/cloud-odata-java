package com.sap.core.odata.core.ep;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.producer.XmlMetadataProducer;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * Provider for all basic (content type independent) entity provider methods.
 * 
 * @author SAP AG
 */
public class BasicEntityProvider {

  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "UTF-8";

  /**
   * Reads binary data from an input stream.
   * @param content the content input stream
   * @return the binary data
   * @throws EntityProviderException
   */
  public byte[] readBinary(InputStream content) throws EntityProviderException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] value = new byte[Short.MAX_VALUE];
    int count;
    try {
      while ((count = content.read(value)) > 0)
        buffer.write(value, 0, count);
      content.close();
      buffer.flush();
      return buffer.toByteArray();
    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /**
   * 
   * @param content
   * @return text as string from <code>InputStream</code>
   * @throws EntityProviderException
   */
  private String readText(InputStream content) throws EntityProviderException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content, Charset.forName(DEFAULT_CHARSET)));
    StringBuilder stringBuilder = new StringBuilder();
    try {
      String line = null;
      while ((line = bufferedReader.readLine()) != null)
        stringBuilder.append(line);
      bufferedReader.close();
    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
    return stringBuilder.toString();
  }

  /**
   * Reads an unformatted value of an EDM property as binary or as content type <code>text/plain</code>.
   * @param edmProperty the EDM property
   * @param content the content input stream
   * @return the value as the proper system data type
   * @throws EntityProviderException
   */
  public Object readPropertyValue(final EdmProperty edmProperty, InputStream content) throws EntityProviderException {
    EdmSimpleType type;
    try {
      type = (EdmSimpleType) edmProperty.getType();
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

    if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance())
      return readBinary(content);
    else
      try {
        return type.valueOfString(readText(content), EdmLiteralKind.DEFAULT, edmProperty.getFacets(), type.getDefaultType());
      } catch (EdmException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
  }

  /**
   * Write property as binary or as content type <code>text/plain</code>.
   * @param edmProperty the EDM property
   * @param value its value
   * @return resulting {@link ODataResponse} with written content
   * @throws EntityProviderException
   */
  public ODataResponse writePropertyValue(final EdmProperty edmProperty, Object value) throws EntityProviderException {
    try {
      final EdmSimpleType type = (EdmSimpleType) edmProperty.getType();

      if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
        String contentType = HttpContentType.APPLICATION_OCTET_STREAM;
        if (edmProperty.getMimeType() != null) {
          contentType = edmProperty.getMimeType();
        } else {
          if (edmProperty.getMapping() != null && edmProperty.getMapping().getMimeType() != null) {
            String mimeTypeMapping = edmProperty.getMapping().getMimeType();
            if (value instanceof Map) {
              final Map<?, ?> mappedData = (Map<?, ?>) value;
              value = mappedData.get(edmProperty.getName());
              contentType = (String) mappedData.get(mimeTypeMapping);
            } else {
              throw new EntityProviderException(EntityProviderException.COMMON);
            }
          }
        }
        return writeBinary(contentType, (byte[]) value);

      } else {
        return writeText(type.valueToString(value, EdmLiteralKind.DEFAULT, edmProperty.getFacets()));
      }

    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /**
   * Write text value as content type <code>text/plain</code>.
   * @param value the string that is written to {@link ODataResponse}
   * @return resulting {@link ODataResponse} with written text content
   * @throws EntityProviderException
   */
  public ODataResponse writeText(final String value) throws EntityProviderException {
    ODataResponseBuilder builder = ODataResponse.newBuilder();
    if (value != null) {
      ByteArrayInputStream stream;
      try {
        stream = new ByteArrayInputStream(value.getBytes(DEFAULT_CHARSET));
      } catch (UnsupportedEncodingException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
      builder.entity(stream);
    }
    builder.contentHeader(ContentType.TEXT_PLAIN_CS_UTF_8.toContentTypeString());
    return builder.build();
  }

  /**
   * Write binary content with content type header set to given <code>mime type</code> parameter.
   * @param mimeType MIME type which is written and used as content type header information
   * @param data data is written to {@link ODataResponse}
   * @return resulting {@link ODataResponse} with written binary content
   * @throws EntityProviderException
   */
  public ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException {
    ODataResponseBuilder builder = ODataResponse.newBuilder();
    if (data != null) {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      builder.entity(bais);
    }
    builder.contentHeader(mimeType);
    return builder.build();
  }

  /**
   * Writes the metadata in XML format. Predefined namespaces is of type Map{@literal <}prefix,namespace{@literal >} and may be null or an empty Map.
   * @param schemas
   * @param predefinedNamespaces
   * @return resulting {@link ODataResponse} with written metadata content
   * @throws EntityProviderException
   */
  public ODataResponse writeMetadata(List<Schema> schemas, Map<String, String> predefinedNamespaces) throws EntityProviderException {
    ODataResponseBuilder builder = ODataResponse.newBuilder();
    String dataServiceVersion = ODataServiceVersion.V10;
    if (schemas != null) {
      dataServiceVersion = calculateDataServiceVersion(schemas);
    }
      DataServices metadata = new DataServices().setSchemas(schemas).setDataServiceVersion(dataServiceVersion);
      OutputStreamWriter writer = null;
      CircleStreamBuffer csb = new CircleStreamBuffer();
      try {
        writer = new OutputStreamWriter(csb.getOutputStream(), "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
      XmlMetadataProducer.writeMetadata(metadata, writer, predefinedNamespaces);
      builder.entity(csb.getInputStream());
    
    builder.contentHeader(ContentType.APPLICATION_XML_CS_UTF_8.toContentTypeString());
    builder.header(ODataHttpHeaders.DATASERVICEVERSION, dataServiceVersion);
    return builder.build();
  }

  /**
   * Calculates the necessary data service version for the metadata serialization
   * @param schemas
   * @return
   */
  private String calculateDataServiceVersion(List<Schema> schemas) {

    String dataServiceVersion = ODataServiceVersion.V10;

    if (schemas != null) {
      for (Schema schema : schemas) {
        List<EntityType> entityTypes = schema.getEntityTypes();
        if (entityTypes != null) {
          for (EntityType entityType : entityTypes) {
            List<Property> properties = entityType.getProperties();
            if (properties != null) {
              for (Property property : properties) {
                if (property.getCustomizableFeedMappings() != null) {
                  if (property.getCustomizableFeedMappings().getFcKeepInContent() != null) {
                    if (!property.getCustomizableFeedMappings().getFcKeepInContent()) {
                      dataServiceVersion = ODataServiceVersion.V20;
                      return dataServiceVersion;
                    }
                  }
                }
              }
              if (entityType.getCustomizableFeedMappings() != null) {
                if (entityType.getCustomizableFeedMappings().getFcKeepInContent() != null) {
                  if (entityType.getCustomizableFeedMappings().getFcKeepInContent()) {
                    dataServiceVersion = ODataServiceVersion.V20;
                    return dataServiceVersion;
                  }
                }
              }
            }
          }
        }
      }
    }

    return dataServiceVersion;
  }
}
