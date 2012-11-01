package com.sap.core.odata.api.edm.provider;

import java.util.List;

public class ComplexType {

  private String name;
  private FullQualifiedName baseType;
  private boolean isAbstract;
  private List<Property> properties;
  //TODO Mapping
  private Documentation documentation;
  private Annotations annotations;

  public ComplexType(String name, FullQualifiedName baseType, boolean isAbstract, List<Property> properties, Documentation documentation, Annotations annotations) {
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

  public List<Property> getProperties() {
    return properties;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}