package com.sap.core.odata.core.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.annotations.ODataSerializer;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.serialization.EdmAnnotationSerializer;

@Provider
public class ODataResponseWriter implements MessageBodyWriter<ODataResponse> {
  
  private static final Logger LOG = LoggerFactory.getLogger(ODataResponseWriter.class);

  @Context UriInfo uriInfo;
  
  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return type == ODataResponse.class;
  }

  @Override
  public long getSize(ODataResponse t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    try {
      return getContent(t).length();
    } catch (IOException e) {
      return -1;
    }
  }

  private String getContent(ODataResponse t) throws IOException {
    ODataSerializer<InputStream> serializer = new EdmAnnotationSerializer(uriInfo.getBaseUri().toString());
    InputStream stream = serializer.serialize(t.getEntity());
    
    String content = streamToString(stream);
    
    return content;
  }

  private String streamToString(InputStream stream) throws IOException {
    int readCount;
    
    StringBuilder b = new StringBuilder();
    
    byte[] buffer = new byte[8192];
    while((readCount = stream.read(buffer)) >= 0) {
      b.append(new String(buffer, 0, readCount));
    }
    
    return b.toString();
  }

  @Override
  public void writeTo(ODataResponse t, Class<?> type, Type genericType, Annotation[] annotations, 
      MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) 
          throws IOException, WebApplicationException {

    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    
    final String content = getContent(t);
    LOG.debug("Write content {}", content);

    entityStream.write(content.getBytes());
  }
  
}
