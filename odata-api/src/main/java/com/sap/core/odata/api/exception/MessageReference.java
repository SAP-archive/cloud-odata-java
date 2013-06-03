package com.sap.core.odata.api.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * APPLICATION DEVELOPERS: Please use {@link ODataApplicationException} to throw custom exceptions. This class is used inside the library only. 
 * <p>A {@link MessageReference} references to the used message for an
 * {@link ODataMessageException} and its sub classes. It supports
 * internationalization and translation of exception messages.
 * <br>Theses classes  contain a {@link MessageReference} object which
 * can be mapped to a related key and message text in the resource bundles.
 * @author SAP AG
 */
public abstract class MessageReference {

  protected final String key;
  protected List<Object> content = null;

  private MessageReference(final String key) {
    this(key, null);
  }

  private MessageReference(final String key, final List<Object> content) {
    this.key = key;
    this.content = content;
  }

  /**
   * Creates a {@link MessageReference} for given <code>class</code> and <code>key</code>.
   * This combination of <code>class</code> and <code>key</code> has to be provided
   * by a resource bundle.
   * @param clazz {@link ODataMessageException} for which this {@link MessageReference}
   *              should be used
   * @param key   unique key (in context of {@link ODataMessageException}) for reference
   *              to message text in resource bundle
   * @return created {@link MessageReference}
   */
  public static MessageReference create(final Class<? extends ODataException> clazz, final String key) {
    return new SimpleMessageReference(clazz.getName() + "." + key);
  }

  public MessageReference create() {
    return new SingleMessageReference(key);
  }

  /**
   * Returns message key.
   */
  public String getKey() {
    return key;
  }

  /**
   * Adds given content to message reference.
   */
  public MessageReference addContent(final Object... content) {
    if (this.content == null) {
      return new SimpleMessageReference(key, content);
    } else {
      final List<Object> mergedContent = new ArrayList<Object>(this.content.size() + content.length);
      mergedContent.addAll(this.content);
      mergedContent.addAll(Arrays.asList(content));
      return new SimpleMessageReference(key, mergedContent);
    }
  }

  /**
   * Receives content for this {@link MessageReference}.
   * Beware that returned list is immutable.
   */
  public List<?> getContent() {
    if (content == null) {
      return Collections.emptyList();
    } else {
      return Collections.unmodifiableList(content);
    }
  }

  /**
   * Simple inner class for realization of {@link MessageReference} interface.
   */
  private static class SimpleMessageReference extends MessageReference {
    public SimpleMessageReference(final String implKey) {
      super(implKey);
    }

    public SimpleMessageReference(final String implKey, final List<Object> content) {
      super(implKey, content);
    }

    public SimpleMessageReference(final String implKey, final Object... content) {
      super(implKey, Arrays.asList(content));
    }
  }

  private static class SingleMessageReference extends MessageReference {
    public SingleMessageReference(final String implKey) {
      super(implKey);
    }

    public SingleMessageReference(final String implKey, final List<Object> content) {
      super(implKey, content);
    }

    public SingleMessageReference(final String implKey, final Object... content) {
      super(implKey, Arrays.asList(content));
    }

    @Override
    public MessageReference addContent(final Object... content) {

      if (this.content == null) {
        this.content = new ArrayList<Object>();
      }

      this.content.addAll(Arrays.asList(content));
      return this;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    return result;
  }

  /**
   * {@link MessageReference}s are equal if their message keys have the same value.
   * @return <code>true</code> if both instances are equal, otherwise <code>false</code>.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    MessageReference other = (MessageReference) obj;
    if (key == null) {
      if (other.key != null) {
        return false;
      }
    } else if (!key.equals(other.key)) {
      return false;
    }
    return true;
  }
}