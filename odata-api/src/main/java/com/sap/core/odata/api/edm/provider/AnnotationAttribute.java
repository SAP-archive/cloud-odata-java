package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;

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

  public AnnotationAttribute setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  public AnnotationAttribute setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public AnnotationAttribute setName(String name) {
    this.name = name;
    return this;
  }

  public AnnotationAttribute setText(String text) {
    this.text = text;
    return this;
  }

}