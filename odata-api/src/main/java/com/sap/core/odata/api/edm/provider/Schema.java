package com.sap.core.odata.api.edm.provider;

import java.util.List;

public class Schema {

  private String namespace;
  private String alias;
  private List<Using> usings;
  private List<EntityType> entityTypes;
  private List<ComplexType> complexTypes;
  private List<Association> associations;
  private List<EntityContainer> entityContainers;
  private Annotations annotations;

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public void setUsings(List<Using> usings) {
    this.usings = usings;
  }

  public void setEntityTypes(List<EntityType> entityTypes) {
    this.entityTypes = entityTypes;
  }

  public void setComplexTypes(List<ComplexType> complexTypes) {
    this.complexTypes = complexTypes;
  }

  public void setAssociations(List<Association> associations) {
    this.associations = associations;
  }

  public void setEntityContainers(List<EntityContainer> entityContainers) {
    this.entityContainers = entityContainers;
  }

  public void setAnnotations(Annotations annotations) {
    this.annotations = annotations;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getAlias() {
    return alias;
  }

  public List<Using> getUsings() {
    return usings;
  }

  public List<EntityType> getEntityTypes() {
    return entityTypes;
  }

  public List<ComplexType> getComplexTypes() {
    return complexTypes;
  }

  public List<Association> getAssociations() {
    return associations;
  }

  public List<EntityContainer> getEntityContainers() {
    return entityContainers;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}