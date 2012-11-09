package com.sap.core.odata.api.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 * A {@link MessageReference} references to the used message for a {@link ODataMessageException} and is used to support 
 * internationalization and translation of exception messages for all {@link ODataMessageException} and sub classes.
 * Theses classes all contains  an {@link MessageReference} object which can be mapped to a related key and message in the resource bundles.
 */
public abstract class MessageReference {

  private final String key;
  private final List<Object> content;
  private HttpStatus httpStatus;

  private MessageReference(String key) {
    this.key = key;
    this.content = new ArrayList<Object>();
  }

  /**
   * Create a {@link MessageReference} for given <code>class</code> and <code>key</code>.
   * These combination of <code>class</code> and <code>key</code> has to be provided by a resource bundle.
   * 
   * @param clazz
   *        {@link ODataMessageException} for which this {@link MessageReference} should be used.
   * @param key
   *        Unique key (in context of {@link ODataMessageException}) for reference to message in resource bundle
   * @return created {@link MessageReference}
   */
  public static MessageReference create(Class<? extends ODataMessageException> clazz, String key) {
    return new SimpleMessageReference(clazz.getName() + "." + key);
  }

  /**
   * Create a {@link MessageReference} for given <code>class</code> and <code>key</code> combination and additionally set
   * given {@link HttpStatus}.
   * These combination of <code>class</code> and <code>key</code> has to be provided by a resource bundle.
   * 
   * @param clazz
   *        {@link ODataMessageException} for which this {@link MessageReference} should be used.
   * @param key
   *        Unique key (in context of {@link ODataMessageException}) for reference to message in resource bundle
   * @param status
   *        {@link HttpStatus} which is set in the created {@link MessageReference}
   * @return created {@link MessageReference}
   */
  public static MessageReference create(Class<? extends ODataMessageException> clazz, String key, HttpStatus status) {
    MessageReference context = create(clazz, key).setHttpStatus(status);
    return context;
  }

  public String getKey() {
    return key;
  }

  public MessageReference addContent(Object... content) {
    for (Object c : content) {
      this.content.add(c);
    }
    return this;
  }

  public List<Object> getContent() {
    return Collections.unmodifiableList(content);
  }
  
  public MessageReference setHttpStatus(HttpStatus status) {
    this.httpStatus = status;
    return this;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }


  /**
   * Simple inner class for realization of {@link MessageReference} interface.
   */
  private static class SimpleMessageReference extends MessageReference {
    public SimpleMessageReference(String implKey) {
      super(implKey);
    }
  }
}