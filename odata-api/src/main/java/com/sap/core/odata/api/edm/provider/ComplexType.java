package com.sap.core.odata.api.edm.provider;

import java.util.Map;

import com.sap.core.odata.api.edm.FullQualifiedName;

public class ComplexType {

  private String name;
  private FullQualifiedName baseType;
  private boolean isAbstract;
  private Map<String, Property> properties;
  private Mapping mapping;
  private Documentation documentation;
  private Annotations annotations;

  public ComplexType(String name, FullQualifiedName baseType, boolean isAbstract, Map<String, Property> properties, Mapping mapping, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.baseType = baseType;
    this.isAbstract = isAbstract;
    this.properties = properties;
    this.documentation = documentation;
    this.annotations = annotations;
  }

  public String getName() {
    return name;
  }

  public FullQualifiedName getBaseType() {
    return baseType;
  }

  public boolean isAbstract() {
    return isAbstract;
  }

  public Map<String, Property> getProperties() {
    return properties;
  }

  public Mapping getMapping() {
    return mapping;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}