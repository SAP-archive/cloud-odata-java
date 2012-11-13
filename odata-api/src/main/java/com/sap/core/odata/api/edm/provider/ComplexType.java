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

  public ComplexType setName(String name) {
    this.name = name;
    return this;
  }

  public ComplexType setBaseType(FullQualifiedName baseType) {
    this.baseType = baseType;
    return this;
  }

  public ComplexType setAbstract(boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  public ComplexType setProperties(Map<String, Property> properties) {
    this.properties = properties;
    return this;
  }

  public ComplexType setMapping(Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  public ComplexType setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public ComplexType setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
  
  
}