package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotationElement;

public class AnnotationElement implements EdmAnnotationElement {

  private String namespace;
  private String prefix;
  private String name;
  private String xmlData;

  public AnnotationElement(String namespace, String prefix, String name, String xmlData) {
    this.namespace = namespace;
    this.prefix = prefix;
    this.name = name;
    this.xmlData = xmlData;
  }

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
}