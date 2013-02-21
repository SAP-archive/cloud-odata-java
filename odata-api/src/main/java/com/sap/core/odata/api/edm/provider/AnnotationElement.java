package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.EdmAnnotationElement;

/**
 * Objects of this class represent an annotation element
 * @author SAP AG
 */
public class AnnotationElement implements EdmAnnotationElement {

  private String namespace;
  private String prefix;
  private String name;
  private String text;
  private List<AnnotationElement> childElements;
  private List<AnnotationAttribute> attributes;

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmAnnotationElement#getNamespace()
   */
  @Override
  public String getNamespace() {
    return namespace;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmAnnotationElement#getPrefix()
   */
  @Override
  public String getPrefix() {
    return prefix;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmAnnotationElement#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmAnnotationElement#getXmlData()
   */
  @Override
  public String getText() {
    return text;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmAnnotationElement#getChildElements()
   */
  @Override
  public List<AnnotationElement> getChildElements() {
    return childElements;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.EdmAnnotationElement#getAttributes()
   */
  @Override
  public List<AnnotationAttribute> getAttributes() {
    return attributes;
  }


  /**
   * Sets the attributes for this {@link AnnotationElement}
   * @param attributes
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setAttributes(List<AnnotationAttribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  /**
   * Sets the child elements for this {@link AnnotationElement}. Do not set child elements and characterData for one element.
   * @param childElements
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setChildElements(List<AnnotationElement> childElements) {
    this.childElements = childElements;
    return this;
  }

  /**
   * Sets the namespace for this {@link AnnotationElement}
   * @param namespace
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets the prefix for this {@link AnnotationElement}
   * @param prefix
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  /**
   * Sets the name for this {@link AnnotationElement}
   * @param name
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the text for this {@link AnnotationElement} which will be displayed inside the tags. Must NOT be set if child elements are set!
   * @param text
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setText(String text) {
    this.text = text;
    return this;
  }

}