package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotationElement;

/**
 * @author SAP AG
  * <p>
 * Objects of this class represent an annotation element in the EDM
 */
public class AnnotationElement implements EdmAnnotationElement {

  private String namespace;
  private String prefix;
  private String name;
  private String xmlData;

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
  public String getXmlData() {
    return xmlData;
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
   * Sets the xmlData for this {@link AnnotationElement}
   * @param xmlData
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setXmlData(String xmlData) {
    this.xmlData = xmlData;
    return this;
  }

}