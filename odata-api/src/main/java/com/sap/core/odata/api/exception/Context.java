package com.sap.core.odata.api.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 * A {@link Context} defines the context in which a {@link ODataMessageException} occurred and is used for support 
 * of internationalization and translated messages for all {@link ODataMessageException} and sub classes.
 * Theses classes all contains  an {@link Context} object which can be mapped to a related key and message in the resource bundles.
 */
public abstract class Context {

  private final String key;
  private final List<Object> values;
  private HttpStatus httpStatus;

  private Context(String key) {
    this.key = key;
    this.values = new ArrayList<Object>();
  }

  public static Context create(Class<? extends ODataMessageException> clazz, String key) {
    return new SimpleContext(clazz.getName() + "." + key);
  }

  public static Context create(Class<? extends ODataMessageException> clazz, String key, HttpStatus status) {
    Context context = create(clazz, key).setHttpStatus(status);
    return context;
  }

  public String getKey() {
    return key;
  }

  public Context addValues(Object... values) {
    for (Object val : values) {
      this.values.add(val);
    }
    return this;
  }

  public List<Object> getValues() {
    return Collections.unmodifiableList(values);
  }
  
  public Context setHttpStatus(HttpStatus status) {
    this.httpStatus = status;
    return this;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  
  private static class SimpleContext extends Context {
    public SimpleContext(String implKey) {
      super(implKey);
    }
  }
}