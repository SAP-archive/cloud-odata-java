package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;

/**
 * Objects of this class represent an annotation attribute
 * @author SAP AG
 */
public class AnnotationAttribute implements EdmAnnotationAttribute {

  private String namespace;
  private String prefix;
  private String name;
  private String text;

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getText() {
    return text;
  }

  /**
   * Sets the namespace for this {@link AnnotationAttribute}.
   * @param namespace
   * @return {@link AnnotationAttribute} for method chaining
   */
  public AnnotationAttribute setNamespace(final String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets the prefix for this {@link AnnotationAttribute}.
   * @param prefix
   * @return {@link AnnotationAttribute} for method chaining
   */
  public AnnotationAttribute setPrefix(final String prefix) {
    this.prefix = prefix;
    return this;
  }

  /**
   * Sets the name for this {@link AnnotationAttribute}.
   * @param name
   * @return {@link AnnotationAttribute} for method chaining
   */
  public AnnotationAttribute setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the text for this {@link AnnotationAttribute}.
   * @param text
   * @return {@link AnnotationAttribute} for method chaining
   */
  public AnnotationAttribute setText(final String text) {
    this.text = text;
    return this;
  }

}