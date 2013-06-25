package com.sap.core.odata.core.rest.app;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.core.rest.ODataExceptionMapperImpl;
import com.sap.core.odata.core.rest.ODataRootLocator;

/**
 * @author SAP AG
 */
public class ODataApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();
    classes.add(ODataRootLocator.class);
    classes.add(ODataExceptionMapperImpl.class);
    classes.add(MyProvider.class);
    return classes;
  }

  /**
   * Singletons are not recommended because they break the state less REST principle.
   */
  @Override
  public Set<Object> getSingletons() {
    return Collections.emptySet();
  }

  @Provider
  @Produces({"generic/value", "multipart/mixed"})
  public static final class MyProvider implements MessageBodyWriter<String> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return (type == String.class);
    }

    @Override
    public long getSize(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return t.length();
    }

    @Override
    public void writeTo(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
      StringBuilder b = new StringBuilder();
      b.append(t);
      entityStream.write(b.toString().getBytes("UTF-8"));
      entityStream.flush();
    }
  }
}
