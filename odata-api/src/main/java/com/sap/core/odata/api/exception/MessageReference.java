package com.sap.core.odata.api.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link MessageReference} references to the used message for a {@link ODataHttpException} and is used to support 
 * internationalization and translation of exception messages for all {@link ODataHttpException} and sub classes.
 * Theses classes all contains  an {@link MessageReference} object which can be mapped to a related key and message in the resource bundles.
 */
public abstract class MessageReference {

  private final String key;
  private final List<Object> content;

  private MessageReference(String key) {
    this.key = key;
    this.content = new ArrayList<Object>();
  }

  /**
   * Create a {@link MessageReference} for given <code>class</code> and <code>key</code>.
   * These combination of <code>class</code> and <code>key</code> has to be provided by a resource bundle.
   * 
   * @param clazz
   *        {@link ODataHttpException} for which this {@link MessageReference} should be used.
   * @param key
   *        Unique key (in context of {@link ODataHttpException}) for reference to message in resource bundle
   * @return created {@link MessageReference}
   */
  public static MessageReference create(Class<? extends ODataException> clazz, String key) {
    return new SimpleMessageReference(clazz.getName() + "." + key);
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


  /**
   * Simple inner class for realization of {@link MessageReference} interface.
   */
  private static class SimpleMessageReference extends MessageReference {
    public SimpleMessageReference(String implKey) {
      super(implKey);
    }
  }
}