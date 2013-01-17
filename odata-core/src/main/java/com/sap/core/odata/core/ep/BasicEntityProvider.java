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

public class BasicEntityProvider implements BasicEntityProviderInterface {

  private static final Logger LOG = LoggerFactory.getLogger(BasicEntityProvider.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "UTF-8";

  @Override
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

  @Override
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
        return type.valueOfString(readText(content), EdmLiteralKind.DEFAULT, edmProperty.getFacets());
      } catch (EdmException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
  }

  @Override
  public ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException {
    OutputStreamWriter writer = null;

    try {
      CircleStreamBuffer csb = new CircleStreamBuffer();
      OutputStream outputStream = csb.getOutputStream();
      writer = new OutputStreamWriter(outputStream, DEFAULT_CHARSET);
      AtomServiceDocumentProducer.writeServiceDocument(edm, serviceRoot, writer);

      ODataResponse response = ODataResponse.entity(csb.getInputStream())
          .contentHeader(createContentHeader(ContentType.APPLICATION_ATOM_SVC))
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

  @Override
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

  @Override
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
    builder.contentHeader(ContentType.TEXT_PLAIN.toContentTypeString());
    return builder.build();
  }

  @Override
  public ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException {
    ODataResponseBuilder builder = ODataResponse.newBuilder();
    if (data != null) {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      builder.entity(bais);
    }
    builder.contentHeader(mimeType);
    return builder.build();
  }

  private String createContentHeader(ContentType mediaType) {
    return mediaType.toString() + "; charset=" + DEFAULT_CHARSET;
  }
}
