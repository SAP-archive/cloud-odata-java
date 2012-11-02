package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;

public class AnnotationAttribute implements EdmAnnotationAttribute {

  private String namespace;
  private String prefix;
  private String name;
  private String text;

  public AnnotationAttribute(String namespace, String prefix, String name, String text) {
    super();
    this.namespace = namespace;
    this.prefix = prefix;
    this.name = name;
    this.text = text;
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
  public String getText() {
    return text;
  }
}