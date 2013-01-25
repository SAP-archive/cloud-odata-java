package com.sap.core.odata.core.ep;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.producer.AtomServiceDocumentProducer;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * Provider for all basic (content type independent) entity provider methods.
 * 
 * @author SAP AG
 */
public class BasicEntityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(BasicEntityProvider.class);
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
   * @return
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
   * Write service document based on given {@link Edm} and <code>service root</code> as
   * content type "<code>application/atomsvc+xml; charset=utf-8</code>".
   * 
   * @param edm the Entity Data Model
   * @param serviceRoot the root URI of the service
   * @return resulting {@link ODataResponse} with written service document
   * @throws EntityProviderException
   */
  public ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException {
    OutputStreamWriter writer = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      OutputStream outputStream = csb.getOutputStream();
      writer = new OutputStreamWriter(outputStream, DEFAULT_CHARSET);
      AtomServiceDocumentProducer.writeServiceDocument(edm, serviceRoot, writer);

      ODataResponse response = ODataResponse.entity(csb.getInputStream())
          .contentHeader(ContentType.APPLICATION_ATOM_SVC_CS_UTF_8.toContentTypeString())
          .build();

      return response;
    } catch (UnsupportedEncodingException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
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
      Map<?, ?> mappedData;
      if (value instanceof Map) {
        mappedData = (Map<?, ?>) value;
        value = mappedData.get(edmProperty.getName());
      } else {
        mappedData = Collections.emptyMap();
      }

      final EdmSimpleType type = (EdmSimpleType) edmProperty.getType();

      if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
        String contentType = ContentType.APPLICATION_OCTET_STREAM.toContentTypeString();
        if (edmProperty.getMimeType() != null) {
          contentType = edmProperty.getMimeType();
        } else {
          if (edmProperty.getMapping() != null && edmProperty.getMapping().getMimeType() != null) {
            String mimeTypeMapping = edmProperty.getMapping().getMimeType();
            contentType = (String) mappedData.get(mimeTypeMapping);
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
}
