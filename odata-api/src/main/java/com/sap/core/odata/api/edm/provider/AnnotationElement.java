package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotationElement;

public class AnnotationElement implements EdmAnnotationElement {

  private String namespace;
  private String prefix;
  private String name;
  private String xmlData;

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
  public String getXmlData() {
    return xmlData;
  }

  public AnnotationElement setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  public AnnotationElement setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public AnnotationElement setName(String name) {
    this.name = name;
    return this;
  }

  public AnnotationElement setXmlData(String xmlData) {
    this.xmlData = xmlData;
    return this;
  }

}