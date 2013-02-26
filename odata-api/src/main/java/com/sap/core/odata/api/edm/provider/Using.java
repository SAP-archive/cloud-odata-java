package com.sap.core.odata.api.edm.provider;

import java.util.Collection;
import java.util.List;

/**
 * @author SAP AG
 */
public class Using {

  private String namespace;
  private String alias;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * Sets the namespace for this {@link Using}
   * @param namespace
   * @return {@link Using} for method chaining
   */
  public Using setNamespace(final String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets the alias for this {@link Using}
   * @param alias
   * @return {@link Using} for method chaining
   */
  public Using setAlias(final String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link Using}
   * @param documentation
   * @return {@link Using} for method chaining
   */
  public Using setDocumentation(final Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link Using}
   * @param annotationAttributes
   * @return {@link Using} for method chaining
   */
  public Using setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link Using}
   * @param annotationElements
   * @return {@link Using} for method chaining
   */
  public Using setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }

  /**
   * @return <b>String</b> namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return <b>String</b> alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return collection of {@link AnnotationAttribute} annotation attributes
   */
  public Collection<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return collection of {@link AnnotationElement} annotation elements
   */
  public Collection<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }
}
