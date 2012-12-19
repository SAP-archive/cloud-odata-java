package com.sap.core.odata.api.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A {@link MessageReference} references to the used message for an {@link ODataHttpException} and is used to support
 * internationalization and translation of exception messages for all {@link ODataHttpException} and sub classes.
 * Theses classes all contains a {@link MessageReference} object which can be mapped to a related key and message in the resource bundles.
 * @author SAP AG
 */
public abstract class MessageReference {

  protected final String key;
  protected List<Object> content = null;

  private MessageReference(String key) {
    this(key, null);
  }

  private MessageReference(String key, List<Object> content) {
    this.key = key;
    this.content = content;
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

  public MessageReference create() {
    return new SimpleMessageReference(this.key);
  }
  
  /**
   * Returns message key.
   */
  public String getKey() {
    return key;
  }

  /**
   * Add given content to message reference.
   */
  public MessageReference addContent(Object... content) {
    return new SimpleMessageReference(this.key, content);
  }

  /**
   * Receive content for this {@link MessageReference}.
   * Beware that returned list is immutable.
   */
  public List<?> getContent() {
    if (content == null)
      return Collections.emptyList();
    else
      return Collections.unmodifiableList(content);
  }

  /**
   * Simple inner class for realization of {@link MessageReference} interface.
   */
  private static class SimpleMessageReference extends MessageReference {
    public SimpleMessageReference(String implKey) {
      super(implKey);
    }

    public SimpleMessageReference(String implKey, List<Object> content) {
      super(implKey, content);
    }

    public SimpleMessageReference(String implKey, Object... content) {
      super(implKey, Arrays.asList(content));
    }
  }
}